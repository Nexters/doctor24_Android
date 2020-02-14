package com.nexters.doctor24.todoc.util

import com.naver.maps.geometry.LatLng

fun LatLng.toDistance(from : LatLng?) : String {
    return String.format("%.2f km", from?.let{this.distanceTo(it)/1000} ?: 0.0)
}