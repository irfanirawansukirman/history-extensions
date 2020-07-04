package com.irfanirawansukirman.pipileman.abstraction.util.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.irfanirawansukirman.pipileman.BuildConfig
import com.irfanirawansukirman.pipileman.R

fun ImageView.load(path: String?, progress: ProgressBar) {
    Glide.with(this)
        .load(BuildConfig.BASE_IMAGE_MOVIE + path)
        .error(R.color.colorAccent)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progress.gone()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progress.gone()
                return false
            }
        })
        .into(this)
}

fun ImageView.loadCircle(path: String?, progress: ProgressBar? = null) {
    Glide.with(this)
        .load(BuildConfig.BASE_IMAGE_MOVIE + path)
        .error(R.color.colorAccent)
        .circleCrop()
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.gone()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.gone()
                return false
            }
        })
        .into(this)
}
