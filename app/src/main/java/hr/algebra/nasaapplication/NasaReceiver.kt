package hr.algebra.nasaapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.nasaapplication.framework.setBooleanPreference
import hr.algebra.nasaapplication.framework.startActivity

class NasaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // set preference DATA_IMPORTED for the day, get new set next day
        context.setBooleanPreference(DATA_IMPORTED, true)

        // redirect on hostActivity
        context.startActivity<HostActivity>()
    }

}