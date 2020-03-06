package com.nexters.doctor24.todoc.api

import com.nexters.doctor24.todoc.data.map.response.ResMapAddress
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

internal interface APIMap {
    @GET("gc")
    suspend fun getMapAddress(
        @Query("coords") coords: String,
        @Query("output") output: String
    ): Response<ResMapAddress>
}