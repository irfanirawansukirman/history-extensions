package com.irfanirawansukirman.extensions.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.irfanirawansukirman.extensions.common.ThrottledOnClickListener

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.notVisible() {
    visibility = View.INVISIBLE
}

fun View.enableView() {
    isClickable = true
    isEnabled = true
    isFocusable = true
}

fun View.disableView() {
    isClickable = false
    isEnabled = false
    isFocusable = false
}

fun View.setOnSingleClickListener(
    holdTime: Long = 1000L,
    action: (View) -> Unit
) {
    setOnClickListener(ThrottledOnClickListener(holdTime) {
        action(it)
    })
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)