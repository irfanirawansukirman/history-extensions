package com.irfanirawansukirman.extensions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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
    moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter = moshi?.adapter<T>(T::class.java)
    val json = adapter?.toJson(obj) ?: "Error"
    logD(json)
}

fun Fragment.logD(message: String) {
    Log.d(this::class.java.simpleName, message)
}

fun Fragment.logE(message: String) {
    Log.e(this::class.java.simpleName, message)
}