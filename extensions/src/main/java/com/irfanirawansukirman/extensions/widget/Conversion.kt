package com.irfanirawansukirman.extensions.widget

import android.text.TextUtils
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Long.toRupiah(): String {
    val localeID = Locale("id", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    return formatRupiah.format(this).replace("Rp", "Rp ")
}

fun String.toNewFormat(
    oldFormat: String,
    newFormat: String,
    isLocale: Boolean = false
): String {
    val dateTimeMillis = if (!TextUtils.isEmpty(this)) {
        SimpleDateFormat(oldFormat, isLocale.isLocaleDate(isLocale)).parse(this).time
    } else {
        0.toLong()
    }

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateTimeMillis

    return if (dateTimeMillis != 0.toLong() && dateTimeMillis != null) {
        SimpleDateFormat(newFormat, isLocale.isLocaleDate(isLocale))
            .format(calendar.time)
    } else {
        SimpleDateFormat(newFormat, isLocale.isLocaleDate(isLocale))
            .format(System.currentTimeMillis())
    }
}

fun String.getLongTime(
    date: String,
    format: String
): Long {
    val dateTimeMillis = if (!TextUtils.isEmpty(date)) {
        SimpleDateFormat(format).parse(date).time
    } else {
        0.toLong()
    }

    return dateTimeMillis
}

fun Boolean.isLocaleDate(
    isLocale: Boolean
): Locale {
    return if (isLocale) Locale("id", "ID")
    else Locale("en", "EN")
}