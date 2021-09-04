package hr.algebra.nasaapplication.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.nasaapplication.NASA_PROVIDER_CONTENT_URI
import hr.algebra.nasaapplication.NasaReceiver
import hr.algebra.nasaapplication.framework.sendBroadcast
import hr.algebra.nasaapplication.handler.downloadImageAndStore
import hr.algebra.nasaapplication.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NasaFetcher(private val context: Context) {

    private val nasaApi: NasaApi


    fun fetchItemsAsync() {
        val request = nasaApi.fetchItems()

        request.enqueue(object : Callback<List<NasaItem>> { // the job is to fetch JSON and And make GSON
            override fun onResponse(
                call: Call<List<NasaItem>>,
                response: Response<List<NasaItem>>
            ) {
                //does a job in background and returns here in foreground
                if (!response.body().isNullOrEmpty()) {
                    populateItems(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<NasaItem>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(nasaItems: List<NasaItem>) {
        // back to background: process GSON and store to db, don't overload ui thread!!!
        GlobalScope.launch {
            nasaItems.forEach {
                val picturePath =
                    downloadImageAndStore(context, it.url, it.title.replace(" ", "_"))
                val values = ContentValues().apply {
                    put(Item::title.name, it.title)
                    put(Item::explanation.name, it.explanation)
                    put(Item::picturePath.name, picturePath ?: "")
                    put(Item::date.name, it.date)
                    put(Item::read.name, false)
                }
                context.contentResolver.insert(NASA_PROVIDER_CONTENT_URI, values)
            }
            context.sendBroadcast<NasaReceiver>()
        }
    }


    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(NASA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nasaApi = retrofit.create(NasaApi::class.java) // this line gives implementation of NasaApi interface
    }
}