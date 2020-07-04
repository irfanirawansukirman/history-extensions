package com.irfanirawansukirman.pipileman.abstraction.util.ext

import android.view.View

fun View.visible() {
    if (visibility == View.GONE) visibility = View.VISIBLE
}

fun View.gone() {
    if (visibility == View.VISIBLE) visibility = View.GONE
}

fun View.invisible() {
    if (visibility == View.VISIBLE) visibility = View.INVISIBLE
}