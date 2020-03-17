package com.nexters.doctor24.todoc.data.mask

import com.nexters.doctor24.todoc.api.mask.APIMaskStore
import com.nexters.doctor24.todoc.data.mask.response.ResMaskStore
import retrofit2.Retrofit

internal class MaskStoreDataSourceImpl(val retrofit: Retrofit) : MaskStoreDataSource {
    override suspend fun getMaskStores(
        page: Int,
        perPage: Int
    ): ResMaskStore = retrofit.create(APIMaskStore::class.java).getMaskStore(
        page = page,
        perPage = perPage
    )
}