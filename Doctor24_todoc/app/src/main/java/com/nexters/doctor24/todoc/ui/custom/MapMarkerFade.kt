package com.nexters.doctor24.todoc.ui.custom

import android.animation.ValueAnimator
import com.naver.maps.map.overlay.Marker

object MapMarkerFade {
    const val FADE_IN = 0
    const val FADE_OUT = 1
    fun fadeIn(marker: Marker, duration: Long, fadeType: Int) {
        val ani: ValueAnimator = when (fadeType) {
            FADE_IN -> ValueAnimator.ofFloat(0f, 1f)
            FADE_OUT -> ValueAnimator.ofFloat(1f, 0f)
            else -> return
        }
        ani.duration = duration
        ani.addUpdateListener { animation: ValueAnimator ->
            marker.alpha = animation.animatedValue as Float
        }
        ani.start()
    }
}