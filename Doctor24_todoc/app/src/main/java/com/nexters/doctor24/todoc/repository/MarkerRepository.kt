package com.nexters.doctor24.todoc.repository

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.data.response.ResMapMarker

internal interface MarkerRepository {
    fun getMarkers(lat:String, long:String, radius: String) : LiveData<List<ResMapMarker>>
}