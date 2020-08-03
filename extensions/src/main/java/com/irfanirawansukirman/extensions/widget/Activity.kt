package com.irfanirawansukirman.extensions.widget

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private var toast: Toast? = null
fun AppCompatActivity.showToast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.navigation(
    withFinish: Boolean = false,
    requestCode: Int = 0,
    intentParams: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java)
    intent.intentParams()
    if (requestCode == 0) startActivityForResult(intent, requestCode) else startActivity(intent)
    if (withFinish) finish()
}

fun AppCompatActivity.isNetworkAvailable(context: Context): Boolean {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected) isConnected = true
    return isConnected ?: false
}