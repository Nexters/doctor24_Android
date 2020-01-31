package com.nexters.doctor24.todoc.data.marker

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.api.APIMarker
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import kotlinx.coroutines.awaitAll
import retrofit2.Retrofit

internal class MarkerDataSourceImpl(val retrofit: Retrofit) : MarkerDataSource {
    override suspend fun getMarkers(lat:String, long:String, type: MarkerTypeEnum): List<ResMapLocation>
            = retrofit.create(APIMarker::class.java).getMarkers(latitude = lat, longitude = long, type = type.type)

    override suspend fun getBounds(
        xlat: String,
        xlong: String,
        zlat: String,
        zlong: String,
        type: MarkerTypeEnum
    ): List<ResMapLocation>
    = retrofit.create(APIMarker::class.java).getBounds(type = type.type, xlat = xlat, xlong = xlong, zlat = zlat, zlong = zlong)
}