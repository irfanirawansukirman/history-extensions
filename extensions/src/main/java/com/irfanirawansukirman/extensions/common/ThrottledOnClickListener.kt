package com.irfanirawansukirman.extensions.common

import android.os.SystemClock
import android.view.View
import java.util.*

/**
 * A Throttled OnClickListener
 * Rejects clicks that are too close together in time.
 * This class is safe to use as an OnClickListener for multiple views, and will throttle each one separately.
 *
 *  * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
 */

class ThrottledOnClickListener(
    private val holdTime: Long,
    private val onClick: (view: View) -> Unit) : View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long> = WeakHashMap()

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[clickedView] = currentTimestamp
        if (previousClickTimestamp == null || currentTimestamp - previousClickTimestamp.toLong() > holdTime) {
            onClick.invoke((clickedView))
        }
    }
}