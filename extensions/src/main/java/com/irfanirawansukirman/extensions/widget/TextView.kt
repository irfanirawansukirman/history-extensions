package com.irfanirawansukirman.extensions.widget

import android.graphics.Paint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView


fun TextView.strikeThru(title: String) {
    text = title
    paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.underLine() {
    val content = text.toString()
    val spanContent = SpannableString(content)
    spanContent.setSpan(UnderlineSpan(), 0, content.length, 0)
    text = spanContent
}