package com.irfanirawansukirman.extensions

import android.content.Context

fun Context.getJsonDataFromAsset(context: Context, fileName: String): String? {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}