package hr.algebra.nasaapplication.api


import retrofit2.Call
import retrofit2.http.GET

const val NASA_API_URL = "https://api.nasa.gov/planetary/"

interface NasaApi {
    @GET("apod?api_key=DEMO_KEY&count=10")
    fun fetchItems(): Call<List<NasaItem>>
}