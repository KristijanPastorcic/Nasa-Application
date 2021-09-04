package hr.algebra.nasaapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import hr.algebra.nasaapplication.databinding.ActivitySplashScreenBinding
import hr.algebra.nasaapplication.framework.*

private const val DELAY : Long = 3000
const val DATA_IMPORTED = "hr.algebra.nasa.data_imported"

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var b: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySplashScreenBinding.inflate(layoutInflater).apply { setContentView(root) }
        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        b.apply {
            ivSplash.applyAnimation(R.anim.rotate)
            tvSplash.applyAnimation(R.anim.blink)
        }
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            //DELAY so splash screen can do its animation
            Handler(Looper.getMainLooper()).postDelayed({startActivity<HostActivity>()}, DELAY)
        } else {
            if (isOnline()) {
                // start service to fetch data
                Intent(this, NasaService::class.java).apply {
                    NasaService.enqueueWork(this@SplashScreenActivity, this)
                }
            } else {
                showToast(getString(R.string.please_connect_to_the_internet))
                finish()
            }
        }
    }
}