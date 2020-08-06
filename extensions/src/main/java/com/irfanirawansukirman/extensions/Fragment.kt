package com.irfanirawansukirman.extensions

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@Suppress("UNCHECKED_CAST")
fun <parent : AppCompatActivity> Fragment.showToast(message: String) {
    (requireActivity() as parent).showToast(message)
}

inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit):
        FRAGMENT = this.apply { arguments = Bundle().apply(argsBuilder) }

fun Fragment.finish() {
    requireActivity().finish()
}

fun Fragment.finishResult(resultCode: Int = 1234, intent: Intent = Intent()) {
    requireActivity().apply {
        setResult(resultCode, intent)
        finish()
    }
}