package com.irfanirawansukirman.extensions

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.irfanirawansukirman.extensions.util.Const.Permission
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// source: https://proandroiddev.com/one-liner-intent-bundle-extras-extractor-in-kotlin-f4b614b390c4
inline fun <reified T: Any> AppCompatActivity.extra(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    if (value is T) value else default
}
inline fun <reified T: Any> AppCompatActivity.extraNotNull(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

// source: https://medium.com/@Zhuinden/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
@Suppress("UNCHECKED_CAST")
inline fun <T : ViewBinding> AppCompatActivity.getViewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    } as T

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

fun AppCompatActivity.navigationModule(
    baseModule: String = "com.irfanirawansukirman",
    targetClass: String,
    withFinish: Boolean = false
) {
    navigationModule(baseModule = baseModule, targetClass = targetClass, withFinish = withFinish) {}
}

// source: https://proandroiddev.com/easy-navigation-in-a-multi-module-android-project-2374ecbaa0ae
fun AppCompatActivity.navigationModule(
    baseModule: String = "com.irfanirawansukirman",
    targetClass: String,
    withFinish: Boolean = false,
    requestCode: Int = 0,
    intentParams: Intent.() -> Unit
) {
    val separator = "."
    val intent = Intent()
    intent.intentParams()
    intent.setClassName(this, baseModule + separator + targetClass)
    if (requestCode != 0) startActivityForResult(intent, requestCode) else startActivity(intent)
    if (withFinish) finish()
    overridePendingTransitionEnter()
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
    overridePendingTransitionEnter()
}

fun AppCompatActivity.navigateToSetting(settingsId: String) {
    startActivity(Intent(settingsId))
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

fun AppCompatActivity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun AppCompatActivity.overridePendingTransitionEnter() {
    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
}

fun AppCompatActivity.overridePendingTransitionExit() {
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
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

inline fun <reified T> AppCompatActivity.encodeToString(obj: T): String {
    return Json.encodeToString(obj)
}

inline fun <reified T> AppCompatActivity.decodeToObj(data: String): T {
    return Json.decodeFromString(data)
}

inline fun <reified T> AppCompatActivity.logD(obj: T) {
    logD(encodeToString(obj))
}

fun AppCompatActivity.logD(message: String) {
    Log.d(this::class.java.simpleName, message)
}

inline fun <reified T> AppCompatActivity.logE(obj: T) {
    logD(encodeToString(obj))
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

fun AppCompatActivity.requestSinglePermission(
    permission: String,
    listener: PermissionListener,
    activity: Activity,
    isSameThread: Boolean = true
) {
    Dexter.withActivity(activity)
        .withPermission(permission)
        .withListener(listener)
        .apply { if (isSameThread) onSameThread() }
        .check()
}

fun AppCompatActivity.requestMultiplePermission(
    permissions: List<String>,
    listener: MultiplePermissionsListener,
    activity: Activity,
    isSameThread: Boolean = true
) {
    Dexter.withActivity(activity)
        .withPermissions(permissions)
        .withListener(listener)
        .apply { if (isSameThread) onSameThread() }
        .check()
}

/*
 * List of self permissions
 */
fun AppCompatActivity.hasCameraPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Permission.CAMERA) == PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.hasWriteExtStoragePermission(): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        Permission.WRITE_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Permission.COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
        this,
        Permission.FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private var locationManager: LocationManager? = null
fun AppCompatActivity.hasGpsEnabled(): Boolean {
    if (locationManager == null) {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
}

fun AppCompatActivity.runHandler(time: Long = 3_000, executeTasks: () -> Unit) {
    Handler().postDelayed({
        executeTasks()
    }, time)
}

fun AppCompatActivity.runCoroutine(
    delayTime: Long = 0L,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    executeTasks: () -> Unit
) {
    GlobalScope.launch(dispatcher) {
        if (delayTime != 0L) {
            delay(delayTime)
        }
        executeTasks()
    }
}

fun AppCompatActivity.getColorCompat(@ColorRes colorId: Int): Int? =
    ContextCompat.getColor(this, colorId)

fun AppCompatActivity.getDrawableCompat(@DrawableRes drawableId: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableId)
