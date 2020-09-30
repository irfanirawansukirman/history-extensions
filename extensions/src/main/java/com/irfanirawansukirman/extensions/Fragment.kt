package com.irfanirawansukirman.extensions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.irfanirawansukirman.extensions.common.FragmentViewBindingDelegate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("UNCHECKED_CAST")
fun <parent : AppCompatActivity> Fragment.getParentActivity() = (requireActivity() as parent)

// source : https://medium.com/@Zhuinden/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit):
        FRAGMENT = this.apply { arguments = Bundle().apply(argsBuilder) }

@Suppress("UNCHECKED_CAST")
fun <parent : AppCompatActivity> Fragment.showToast(message: String) {
    (requireActivity() as parent).showToast(message)
}

@Suppress("UNCHECKED_CAST")
fun <parent : AppCompatActivity> Fragment.showSnackBar(
    v: View,
    message: String = "",
    actionTitle: String? = null,
    action: () -> Unit
) {
    (requireActivity() as parent).showSnackBar(v, message, actionTitle) {
        action()
    }
}

@Suppress("UNCHECKED_CAST")
fun <parent : AppCompatActivity> Fragment.finish() {
    (requireActivity() as parent).finish()
}

@Suppress("UNCHECKED_CAST")
fun <parent : AppCompatActivity> Fragment.finishResult(
    resultCode: Int = 1234,
    intent: Intent = Intent()
) {
    (requireActivity() as parent).finishResult()
}

inline fun <reified parent : AppCompatActivity> Fragment.navigation() {
    navigation<parent> { }
}

inline fun <reified parent : AppCompatActivity> Fragment.navigation(
    withFinish: Boolean = false,
    requestCode: Int = 0,
    intentParams: Intent.() -> Unit
) {
    (requireActivity() as parent).navigation<parent>(withFinish, requestCode, intentParams)
}

inline fun <reified T> Fragment.logD(obj: T) {
    logD(Json.encodeToString(obj))
}

fun Fragment.logD(message: String) {
    Log.d(this::class.java.simpleName, message)
}

fun Fragment.logE(message: String) {
    Log.e(this::class.java.simpleName, message)
}