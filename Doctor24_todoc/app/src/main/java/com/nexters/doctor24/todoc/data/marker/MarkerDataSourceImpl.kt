package com.nexters.doctor24.todoc.data.marker

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.api.APIMarker
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import kotlinx.coroutines.awaitAll
import retrofit2.Retrofit

internal class MarkerDataSourceImpl(val retrofit: Retrofit) : MarkerDataSource {
    override suspend fun getMarkers(lat:String, long:String, type: MarkerTypeEnum): List<ResMapMarker> = retrofit.create(
        APIMarker::class.java).getMarkers(latitude = lat, longitude = long, type = type.type)
}