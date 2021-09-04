package hr.algebra.nasaapplication.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import androidx.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import hr.algebra.nasaapplication.NASA_PROVIDER_CONTENT_URI
import hr.algebra.nasaapplication.model.Item

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showToast(text: String) {
    with(Toast.makeText(this, text, Toast.LENGTH_SHORT)) {
        show()
    }
}

fun isExternalStorageWritable(): Boolean {
    return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}

fun isExternalStorageReadable(): Boolean {
    return isExternalStorageWritable() ||
            Environment.MEDIA_MOUNTED_READ_ONLY == Environment.getExternalStorageState()
}


fun View.applyAnimation(resourceId: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, resourceId))

inline fun <reified T : Activity> Context.startActivity() =
    startActivity(Intent(this, T::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })

inline fun <reified T : Activity> Context.startActivity(key: String, value: Int) =
    startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key, value)
    })

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getBooleanPreference(key: String): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)
}

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    if (network != null) {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        if (networkCapabilities != null) {
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun Context.fetchItems(): MutableList<Item> {
    val items = mutableListOf<Item>()
    val cursor = contentResolver?.query(NASA_PROVIDER_CONTENT_URI, null, null, null, null)
    if (cursor != null) {
        while (cursor.moveToNext()) {
            items.add(
                Item(
                    cursor.getLong(cursor.getColumnIndex((Item::_id.name))),
                    cursor.getString(cursor.getColumnIndex((Item::title.name))),
                    cursor.getString(cursor.getColumnIndex((Item::explanation.name))),
                    cursor.getString(cursor.getColumnIndex((Item::picturePath.name))),
                    cursor.getString(cursor.getColumnIndex((Item::date.name))),
                    cursor.getInt(cursor.getColumnIndex((Item::read.name))) == 1
                )
            )
        }
    }

    return items
}