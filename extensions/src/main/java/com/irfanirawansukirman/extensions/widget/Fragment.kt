package com.irfanirawansukirman.extensions.widget

import android.widget.Toast
import androidx.fragment.app.Fragment

private var toast: Toast? = null
fun Fragment.showToast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
    toast?.show()
}