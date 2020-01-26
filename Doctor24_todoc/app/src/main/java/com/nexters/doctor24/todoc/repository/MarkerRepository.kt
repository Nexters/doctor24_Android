package com.nexters.doctor24.todoc.repository

import androidx.lifecycle.LiveData
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker

internal interface MarkerRepository {
    suspend fun getMarkers(lat:String, long:String, type: MarkerTypeEnum) : List<ResMapMarker>
    suspend fun getBounds(zLocation: LatLng, xLocation: LatLng, type: MarkerTypeEnum) : List<ResMapMarker>
}