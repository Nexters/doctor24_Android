package com.nexters.doctor24.todoc.api.mask

import com.nexters.doctor24.todoc.data.mask.response.ResStoreSaleResult
import retrofit2.http.GET
import retrofit2.http.Query

internal interface APIMaskStore{
    @GET("storesByGeo/json")
    suspend fun getMaskStoreByGeo(
        @Query("lat") page : Float,
        @Query("lng") perPage : Float,
        @Query("m") radius : Int
    ) : ResStoreSaleResult
}