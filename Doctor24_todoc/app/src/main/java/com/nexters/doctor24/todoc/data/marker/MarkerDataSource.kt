package com.nexters.doctor24.todoc.data.marker

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker

internal interface MarkerDataSource {
    suspend fun getMarkers(lat:String, long:String, type: MarkerTypeEnum): List<ResMapMarker>
    suspend fun getBounds(xlat: String, xlong: String, zlat: String, zlong:String, type: MarkerTypeEnum): List<ResMapMarker>
}