package com.nexters.doctor24.todoc.data

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.api.APIMarker
import com.nexters.doctor24.todoc.data.response.ResMapMarker
import retrofit2.Retrofit

internal class MarkerDataSourceImpl(private val retrofit: Retrofit) : MarkerDataSource{
    override fun getMarkers(lat:String, long:String, radius: String): LiveData<List<ResMapMarker>> = retrofit.create(
        APIMarker::class.java).getMarkers(lat, long, radius)
}