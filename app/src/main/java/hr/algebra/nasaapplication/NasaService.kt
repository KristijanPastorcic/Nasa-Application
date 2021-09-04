package hr.algebra.nasaapplication

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.nasaapplication.api.NasaFetcher

private const val JOB_ID = 1

class NasaService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        // fetch Items & store in db run NasaReceiver
        NasaFetcher(this).fetchItemsAsync()
    }

    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NasaService::class.java, JOB_ID, intent)
        }
    }
}