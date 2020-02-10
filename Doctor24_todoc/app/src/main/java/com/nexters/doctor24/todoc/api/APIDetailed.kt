package com.nexters.doctor24.todoc.api

import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface APIDetailed {
    @GET("medicals/{type}/facilities/{facilityId}")
    suspend fun getDetailedInfo(
        @Path("type") type: String,
        @Path("facilityId") facilityId: String
    ): Response<DetailedInfoData>
}