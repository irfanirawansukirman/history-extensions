package com.irfanirawansukirman.pipileman.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irfanirawansukirman.extensions.showToast
import com.irfanirawansukirman.pipileman.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val param = intent?.getStringExtra("a") ?: "Haha"
        showToast(param)
    }
}