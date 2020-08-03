package com.irfanirawansukirman.extensions.widget

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.notVisible() {
    visibility = View.INVISIBLE
}