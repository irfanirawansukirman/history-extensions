package com.irfanirawansukirman.extensions.util

import android.Manifest

object Const {
    object Permission {
        const val CAMERA = Manifest.permission.CAMERA
        const val CAMERA_NAME = "android.permission.CAMERA"

        const val WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}