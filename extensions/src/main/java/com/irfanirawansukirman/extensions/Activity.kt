package com.irfanirawansukirman.extensions

import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private var toast: Toast? = null
fun AppCompatActivity.showToast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}

fun AppCompatActivity.showSnackBar(
    v: View,
    message: String = "",
    actionTitle: String? = null,
    action: () -> Unit
) {
    Snackbar.make(v, message, Snackbar.LENGTH_SHORT).setAction(actionTitle) {
        action()
    }.show()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.navigation() {
    navigation<T> {}
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.navigation(
    withFinish: Boolean = false,
    requestCode: Int = 0,
    intentParams: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java)
    intent.intentParams()
    if (requestCode != 0) startActivityForResult(intent, requestCode) else startActivity(intent)
    if (withFinish) finish()
}

fun AppCompatActivity.finishResult(resultCode: Int = 1234) {
    finishResult(resultCode) {}
}

fun AppCompatActivity.finishResult(
    resultCode: Int = 1234,
    intent: Intent = Intent(),
    intentParams: Intent.() -> Unit
) {
    intent.intentParams()
    setResult(resultCode, intent)
    finish()
}

fun AppCompatActivity.isNetworkAvailable(context: Context): Boolean {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected) isConnected = true
    return isConnected ?: false
}

fun AppCompatActivity.createDialog(
    @LayoutRes layoutId: Int,
    execute: (Dialog) -> Unit
) {
    val dialog = Dialog(this)
    dialog.apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layoutId)
        window?.apply {
            setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }
    execute(dialog)
}

private var notificationManager: NotificationManager? = null
fun AppCompatActivity.createNotification(
    title: String = "Your Title",
    message: String = "Your Message",
    channelId: String = "12345",
    channelName: String = "Your Channel Name",
    channelDescription: String = "Your Channel Description",
    notificationId: Int = 1234,
    smallIcon: Int = R.drawable.ic_notification,
    expertConfig: (NotificationCompat.Builder) -> Unit
) {
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
        setContentTitle(title)
        setContentText(message)
        setSmallIcon(smallIcon)
        setAutoCancel(true)
        priority = NotificationCompat.PRIORITY_MAX
        setSound(defaultSoundUri)
    }

    expertConfig(notificationBuilder)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel
        val mChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        mChannel.description = channelDescription
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager?.createNotificationChannel(mChannel)
    }

    notificationManager?.notify(notificationId, notificationBuilder.build())
}

fun AppCompatActivity.clearNotification() = notificationManager?.cancelAll()

var moshi: Moshi? = null
inline fun <reified T> AppCompatActivity.logD(obj: T) {
    moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter = moshi?.adapter<T>(T::class.java)
    val json = adapter?.toJson(obj) ?: "Error"
    logD(json)
}

fun AppCompatActivity.logD(message: String) {
    Log.d(this::class.java.simpleName, message)
}

inline fun <reified T> AppCompatActivity.logE(obj: T) {
    moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter = moshi?.adapter<T>(T::class.java)
    val json = adapter?.toJson(obj) ?: "Error"
    logE(json)
}

fun AppCompatActivity.logE(message: String) {
    Log.e(this::class.java.simpleName, message)
}

fun AppCompatActivity.getScreenWidth(): Int {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

fun AppCompatActivity.getScreenHeight(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

fun AppCompatActivity.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun AppCompatActivity.singlePermission() {
    // coming soon
}

fun AppCompatActivity.multiplePermission() {
    // coming soon
}