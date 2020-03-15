package com.nexters.doctor24.todoc.api.mask

import com.nexters.doctor24.todoc.data.mask.response.ResMaskStore
import retrofit2.http.GET
import retrofit2.http.Query

internal interface APIMaskStore{
    @GET("/stores/json")
    suspend fun getMaskStore(
        @Query("page") page : Int,
        @Query("perPage") perPage : Int
    ) : ResMaskStore
}