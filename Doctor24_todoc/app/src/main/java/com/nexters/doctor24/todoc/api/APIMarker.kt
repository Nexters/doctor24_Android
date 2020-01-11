package com.nexters.doctor24.todoc.api

import androidx.lifecycle.LiveData
import com.nexters.doctor24.todoc.data.response.ResMapMarker
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

internal interface  APIMarker {
    @GET("medicals/hospital")
    fun getMarkers(
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("radiusKilometer") radiusKilometer: String
    ): LiveData<List<ResMapMarker>>
}