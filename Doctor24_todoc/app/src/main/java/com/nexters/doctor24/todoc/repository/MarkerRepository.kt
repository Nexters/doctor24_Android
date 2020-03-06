package com.nexters.doctor24.todoc.repository

import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation

internal interface MarkerRepository {
    suspend fun getMarkers(center : LatLng, type: MarkerTypeEnum, level: Int?=null, category: String? = null, startTime: String?=null, endTime: String?=null) : List<ResMapLocation>
    suspend fun getBounds(zLocation: LatLng, xLocation: LatLng, type: MarkerTypeEnum) : List<ResMapLocation>
}