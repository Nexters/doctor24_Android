package com.nexters.doctor24.todoc.repository

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.data.MarkerDataSource
import com.nexters.doctor24.todoc.data.response.ResMapMarker
import retrofit2.Retrofit

internal class MarkerRepositoryImpl(private val dataSource: MarkerDataSource): MarkerRepository {
    override fun getMarkers(lat:String, long:String, radius: String): LiveData<List<ResMapMarker>> {
        return dataSource.getMarkers(lat, long, radius)
    }
}