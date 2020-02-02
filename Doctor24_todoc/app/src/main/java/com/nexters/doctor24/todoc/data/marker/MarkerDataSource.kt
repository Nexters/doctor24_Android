package com.nexters.doctor24.todoc.data.marker

import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation

internal interface MarkerDataSource {
    suspend fun getMarkers(center : LatLng, type: MarkerTypeEnum, level: Int?): List<ResMapLocation>
    suspend fun getBounds(xlat: String, xlong: String, zlat: String, zlong:String, type: MarkerTypeEnum): List<ResMapLocation>
}