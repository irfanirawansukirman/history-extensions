package com.irfanirawansukirman.extensions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.security.MessageDigest
import java.util.regex.Pattern

fun Any?.isNull() = this == null

fun Any?.isNotNull() = !isNull()

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0L

fun <T> T?.orDefault(default: T): T {
    return this ?: default
}

fun Int?.toDp() = (this?.div(Resources.getSystem().displayMetrics.density))?.toInt() ?: 0

fun Int?.toPx() = (this?.times((Resources.getSystem().displayMetrics.density)))?.toInt() ?: 0

/**
 * Convert Celsius temperature to Fahrenheit
 */
fun Double?.toFahrenheit(): Double = (this?.times(1.8))?.plus(32) ?: 0.0

/**
 * Convert Fahrenheit temperature to Celsius
 */
fun Double?.toCelsius(): Double = ((this?.minus(32))?.times(5) ?: 0.0) / 9

// source : https://medium.com/better-programming/10-useful-kotlin-string-extensions-46772b653f71
// =================================================================================================
val String.md5: String
    get() {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }

val String.sha1: String
    get() {
        val bytes = MessageDigest.getInstance("SHA-1").digest(this.toByteArray())
        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

// how to use : https://github.com/MichaelRocks/libphonenumber-android/blob/master/sample/src/main/java/io/michaelrocks/libphonenumber/android/sample/MainActivity.java
fun String.formatToLocaleRegion(context: Context, region: String = "ID"): String? {
    val phoneNumberKit = PhoneNumberUtil.createInstance(context)
    val number = phoneNumberKit.parse(this, region)
    if (!phoneNumberKit.isValidNumber(number))
        return null

    return phoneNumberKit.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
}

fun String.isPhoneValid(context: Context, region: String = "ID"): Boolean {
    val phoneNumberKit = PhoneNumberUtil.createInstance(context)
    val number = phoneNumberKit.parse(this, region)
    return phoneNumberKit.isValidNumber(number)
}
// =================================================================================================

/**
 * Wrapping try/catch to ignore catch block
 */
inline fun <T> justTry(default: T, block: () -> T) = try {
    block()
} catch (e: Throwable) {
    default
}

/**
 * App's debug mode
 */
inline fun debugMode(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

/**
 * For functionality supported above API 21 (Eg. Material design stuff)
 */
inline fun lollipopAndAbove(block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        block()
    }
}

/**
 * For functionality supported above API 21 (Eg. Material design stuff)
 */
inline fun marshmallowAndAbove(block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        block()
    }
}