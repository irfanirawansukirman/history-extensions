package com.irfanirawansukirman.extensions.widget

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.irfanirawansukirman.extensions.common.GlideApp

fun ImageView.load(
    path: String?,
    progress: ProgressBar,
    @DrawableRes error: Int,
    @DrawableRes placeholder: Int
) {
    GlideApp.with(this)
        .load(path)
        .error(error)
        .placeholder(placeholder)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progress.hide()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progress.hide()
                return false
            }
        })
        .into(this)
}

fun ImageView.loadCircle(
    path: String?,
    progress: ProgressBar? = null,
    @DrawableRes error: Int,
    @DrawableRes placeholder: Int
) {
    GlideApp.with(this)
        .load(path)
        .error(error)
        .placeholder(placeholder)
        .circleCrop()
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.hide()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.hide()
                return false
            }
        })
        .into(this)
}