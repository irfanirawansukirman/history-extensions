package com.irfanirawansukirman.extensions.widget

import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun Long.toRupiah(symbol: String = "Rp", separatorGroup: Char = '.'): String {
    val decimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val symbolSeparatorFormat = DecimalFormatSymbols()

    symbolSeparatorFormat.apply {
        currencySymbol = "$symbol "
        groupingSeparator = separatorGroup
    }

    decimalFormat.decimalFormatSymbols = symbolSeparatorFormat

    return decimalFormat
        .format(this)
}

fun String.setDefault(clearAfterComma: Boolean = true): String {
    val text = replace("RP", "Rp")
    return if (clearAfterComma) {
        text.replace(",00", "")
    } else {
        text
    }
}

fun Long.toNewFormat(
    newFormat: String = "dd/MM/yyyy",
    isLocale: Boolean = true
): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    return if (this != 0.toLong() && this != null) SimpleDateFormat(
        newFormat,
        isLocale.isLocaleDate(isLocale)
    )
        .format(calendar.time) else SimpleDateFormat(newFormat, isLocale.isLocaleDate(isLocale))
        .format(System.currentTimeMillis())
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

fun String.getLongFromDate(format: String = "dd/MM/yyyy"): Long {
    val dateTimeMillis = if (!TextUtils.isEmpty(this)) {
        SimpleDateFormat(format).parse(this).time
    } else {
        0.toLong()
    }

    return dateTimeMillis
}

fun String.webviewJustifyContent(): String {
    val head = "<head><style>img{max-width: 100%; height: auto;} body { margin: 0; }" +
            "iframe {display: block; background: #000; border-top: 4px solid #000; border-bottom: 4px solid #000;" +
            "top:0;left:0;width:100%;height:235;}</style></head>"
    return "<html>$head<body>$this</body></html>"
}

fun Boolean.isLocaleDate(
    isLocale: Boolean
): Locale {
    return if (isLocale) Locale("id", "ID")
    else Locale("en", "EN")
}

fun String.emailValid(): Boolean {
    val emailPattern = Pattern.compile(
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    )
    return emailPattern.matcher(this).matches()
}

fun String.phoneValid(): Boolean {
    return PhoneNumberUtils.isGlobalPhoneNumber(this)
}