package com.nexters.doctor24.todoc.data.marker

import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation

internal interface MarkerDataSource {
    suspend fun getMarkers(lat:String, long:String, type: MarkerTypeEnum): List<ResMapLocation>
    suspend fun getBounds(xlat: String, xlong: String, zlat: String, zlong:String, type: MarkerTypeEnum): List<ResMapLocation>
}