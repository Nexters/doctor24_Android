package com.nexters.doctor24.todoc.api

import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface  APIMarker {
    @GET("medicals/{type}/facilities")
    suspend fun getMarkers(
        @Path("type") type: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): List<ResMapMarker>
}