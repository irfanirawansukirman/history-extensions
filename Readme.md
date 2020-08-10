# History Extension Library

This is a collection of android kotlin extensions that I often use while developing android applications. I like open source, therefore I hope this can be useful and help speed up the application development process and reduce code boilerplate for android developers.

## Getting Started

This library uses androidX. So, before you use this library, please migrate your project dependencies to androidX and you can see the documentation in [here](https://medium.com/androiddevelopers/migrating-to-androidx-tip-tricks-and-guidance-88d5de238876).

### Installing

Add it to your build.gradle (project):

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

and add it to your build.gradle (module):

```
implementation 'com.github.irfanirawansukirman:history-extensions:0.0.4'
```

## Table of Contents
#### [1.Activity Scope](#activity-scope)
- [Show Toast](#show-toast)
- [Show SnackBar](#show-snackbar)
- [Navigation](#navigation)
- [Finish With Result](#finish-with-result)
- [Connection Checking](#connection-checking)
- [Custom Dialog](#custom-dialog)
- [Notification](#notification)
- [Log](#log) (Object logging use moshi for serialization and deserialization)
- [Screen Width](#screen-width)
- [Screen Height](#screen-height)
- [StatusBar Height](#statusbar-height)
#### [2.Fragment](https://github.com/irfanirawansukirman/history-extensions#fragment)
#### [3.LiveData](https://github.com/irfanirawansukirman/history-extensions#livedata) 
#### [4.UI](https://github.com/irfanirawansukirman/history-extensions#usage-in-kotlin)
- [View](https://github.com/irfanirawansukirman/history-extensions#view)
- [TextView](https://github.com/irfanirawansukirman/history-extensions#textview)
- [RecyclerView](https://github.com/irfanirawansukirman/history-extensions#recyclerview) 
- [ImageView](https://github.com/irfanirawansukirman/history-extensions#encryptentity) 
#### [5.Conversion](https://github.com/irfanirawansukirman/history-extensions#imageview)

## Activity Scope
#### Show Toast
```
private var toast: Toast? = null
fun AppCompatActivity.showToast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         showToast("Your message")
     } 
}
```

#### Show SnackBar 
```
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
```
#### How to use?
Without action button:
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         showSnackBar(yourView, "Your message") {}
     } 
}
```
With action button:
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         showSnackBar(yourView, "Your message", "Your action button title") {
             // your task
         }
     } 
}
```
#### Navigation
```
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
```
#### How to use?
Without custom parameter (request_code default is 0):
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         navigation<yourActivityTarget> {
             // put params here
         }
     } 
}
```
With custom parameter:
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         navigation<yourActivityTarget>(requestCode = yourRequestCode, withFinish = true or false) {
             // put params here
         }
     } 
}
```
#### Finish With Result
```
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
```
#### How to use?
Without custom parameter (result_code default is 1234): 
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         finishResult {
             // put params here
         }
     } 
}
```
With custom parameter:
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         finishResult(resultCode = yourResultCode, intent = yourIntentTarget) {
             // put params here            
         }
     } 
}
```
#### Connection Checking
```
fun AppCompatActivity.isNetworkAvailable(context: Context): Boolean {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected) isConnected = true
    return isConnected ?: false
}
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         if (isNetworkAvailable(yourContext)) {} else {}
     } 
}
```
#### Custom Dialog
```
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
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         createDialog(R.layout.yourLayoutDialog) {
             // your dialog reference                
         }
     } 
}
```
#### Notification
```
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
```
#### Cancel Notification
```
fun AppCompatActivity.clearNotification() = notificationManager?.cancelAll()
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
          createNotification {
               val intentTarget = Intent(yourContext, yourActivityTarget::class.java).apply {
                   // your custom param and flag
               }
               val pendingIntent =
                   PendingIntent.getActivity(yourContext, yourRequestCode, intentTarget, yourFlags)
               it.apply {
                   setContentIntent(pendingIntent)
               }
          }

          // if you want cancel notification
          clearNotification()
     } 
}
```
#### Log
```
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
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         // debug
         logD(yourObject)
         // or
         logD("yourMessage")

         // error
         logE(yourObject)
         // or
         logE("yourMessage")
     } 
} 
```
#### Screen Width
```
fun AppCompatActivity.getScreenWidth(): Int {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         val screenWidth = getScreenWidth()
     } 
}     
```
#### Screen Height
```
fun AppCompatActivity.getScreenHeight(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         val screenHeight = getScreenHeight()
     } 
}     
```
#### StatusBar Height
```
fun AppCompatActivity.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}
```
#### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
         val statusBarHeight = getStatusBarHeight()
     } 
}     
```