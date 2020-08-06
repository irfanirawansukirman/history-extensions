package com.irfanirawansukirman.extensions.widget

import android.content.Context
import androidx.recyclerview.widget.*

fun RecyclerView.setLinearList(
    orientation: Int = RecyclerView.VERTICAL,
    isReverse: Boolean = false
) {
    layoutManager = LinearLayoutManager(context, orientation, isReverse)
    setHasFixedSize(true)
    itemAnimator = DefaultItemAnimator()
    isNestedScrollingEnabled = false
    setItemViewCacheSize(30)
    isDrawingCacheEnabled = true
    drawingCacheQuality = android.view.View.DRAWING_CACHE_QUALITY_HIGH
}

fun RecyclerView.setGridList(numberOfColumns: Int) {
    layoutManager = GridLayoutManager(context, numberOfColumns)
    setHasFixedSize(true)
    itemAnimator = DefaultItemAnimator()
    isNestedScrollingEnabled = false
    setItemViewCacheSize(30)
    isDrawingCacheEnabled = true
    drawingCacheQuality = android.view.View.DRAWING_CACHE_QUALITY_HIGH
}

fun RecyclerView.setStaggeredList(
    numberOfColumns: Int,
    orientation: Int = StaggeredGridLayoutManager.VERTICAL
) {
    layoutManager = StaggeredGridLayoutManager(numberOfColumns, orientation)
    itemAnimator = DefaultItemAnimator()
    isNestedScrollingEnabled = false
    setItemViewCacheSize(30)
    isDrawingCacheEnabled = true
    drawingCacheQuality = android.view.View.DRAWING_CACHE_QUALITY_HIGH
}

fun RecyclerView.setfitColumnsGrid(
    context: Context
): Int {
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / 180).toInt()
}