package com.nexters.doctor24.todoc.repository

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.data.marker.MarkerDataSource
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker

internal class MarkerRepositoryImpl(private val dataSource: MarkerDataSource): MarkerRepository {
    override suspend fun getMarkers(lat:String, long:String, type: MarkerTypeEnum): List<ResMapMarker> {
        return dataSource.getMarkers(lat, long, type)
    }
}