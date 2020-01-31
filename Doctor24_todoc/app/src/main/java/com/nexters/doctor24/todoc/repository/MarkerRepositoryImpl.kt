package com.nexters.doctor24.todoc.repository

import androidx.lifecycle.LiveData
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.data.marker.MarkerDataSource
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker

internal class MarkerRepositoryImpl(private val dataSource: MarkerDataSource): MarkerRepository {
    override suspend fun getMarkers(lat:String, long:String, type: MarkerTypeEnum): List<ResMapLocation> {
        return dataSource.getMarkers(lat, long, type)
    }

    override suspend fun getBounds(
        zLocation: LatLng,
        xLocation: LatLng,
        type: MarkerTypeEnum
    ): List<ResMapLocation> {
        return dataSource.getBounds(xlat = xLocation.latitude.toString(), xlong = xLocation.longitude.toString(),
            zlat = zLocation.latitude.toString(), zlong = zLocation.longitude.toString(), type = type)
    }
}