package com.nexters.doctor24.todoc.data

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.data.response.ResMapMarker

internal interface MarkerDataSource {
    fun getMarkers(lat:String, long:String, radius: String): LiveData<List<ResMapMarker>>
}