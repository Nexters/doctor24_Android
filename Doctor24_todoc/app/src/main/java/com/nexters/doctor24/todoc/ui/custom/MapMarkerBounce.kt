package com.nexters.doctor24.todoc.ui.custom

import android.graphics.PointF
import android.os.Handler
import android.os.SystemClock
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import com.naver.maps.map.overlay.Marker

class MapMarkerBounce {
    private val mHandler: Handler = Handler()
    private var mAnimation: Runnable? = null
    fun onMarkerClick(
        marker: Marker,
        duration: Long,
        anchorX: Float,
        anchorY: Float
    ): Boolean { // This causes the marker at Perth to bounce into position when it is clicked.
        val start = SystemClock.uptimeMillis()
        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation)
        // Starts the bounce animation
        mAnimation = BounceAnimation(start, duration, anchorX, anchorY, marker, mHandler)
        mHandler.post(mAnimation)
        // for the default behavior to occur (which is for the camera to move such that the
// marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    /**
     * Performs a bounce animation on a [Marker].
     */
    private class BounceAnimation internal constructor(
        private val mStart: Long, private val mDuration: Long, anchorX: Float, anchorY: Float,
        marker: Marker, handler: Handler
    ) : Runnable {
        private val mInterpolator: Interpolator
        private val mMarker: Marker = marker
        private val mHandler: Handler = handler
        private val mAnchorX: Float = anchorX
        private val mAnchorY: Float = anchorY
        override fun run() {
            val elapsed = SystemClock.uptimeMillis() - mStart
            val t = Math.max(
                1 - mInterpolator.getInterpolation(elapsed.toFloat() / mDuration),
                0f
            )

            mMarker.anchor = PointF(mAnchorX, mAnchorY + t / 3f)
            if (t > 0) { // Post again 16ms later.
                mHandler.postDelayed(this, 16L)
            }
        }

        init {
            mInterpolator = BounceInterpolator()
        }
    }

}