package com.nexters.doctor24.todoc.data.mask

import com.nexters.doctor24.todoc.api.mask.APIMaskStore
import com.nexters.doctor24.todoc.data.mask.response.ResStoreSaleResult
import retrofit2.Retrofit

internal class MaskStoreDataSourceImpl(private val retrofit: Retrofit) : MaskStoreDataSource {
    override suspend fun getMaskStores(lat: Float, lng: Float, m: Int): ResStoreSaleResult
        = retrofit.create(APIMaskStore::class.java).getMaskStoreByGeo(lat, lng, m)
}