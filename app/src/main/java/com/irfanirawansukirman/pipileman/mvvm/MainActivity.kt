package com.irfanirawansukirman.pipileman.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.irfanirawansukirman.extensions.createDialog
import com.irfanirawansukirman.pipileman.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val param = intent?.getStringExtra("a") ?: "Haha"
        txtTitle.text = param

        btnFinish.setOnClickListener {
            createDialog(R.layout.sample_dialog) {

            }
        }
    }
}