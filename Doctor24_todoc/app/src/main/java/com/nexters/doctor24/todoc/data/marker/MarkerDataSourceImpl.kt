package com.nexters.doctor24.todoc.data.marker

import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.api.APIMarker
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation
import retrofit2.Retrofit

internal class MarkerDataSourceImpl(val retrofit: Retrofit) : MarkerDataSource {
    override suspend fun getMarkers(
        center: LatLng,
        type: MarkerTypeEnum,
        level: Int?,
        category: String?
    ): List<ResMapLocation> = retrofit.create(APIMarker::class.java).getMarkers(
        latitude = center.latitude.toString(),
        longitude = center.longitude.toString(),
        type = type.type,
        radiusLevel = level,
        category = category
    )

    override suspend fun getBounds(
        xlat: String,
        xlong: String,
        zlat: String,
        zlong: String,
        type: MarkerTypeEnum
    ): List<ResMapLocation> = retrofit.create(APIMarker::class.java).getBounds(
        type = type.type,
        xlat = xlat,
        xlong = xlong,
        zlat = zlat,
        zlong = zlong
    )
}