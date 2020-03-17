package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.data.mask.MaskStoreDataSource
import com.nexters.doctor24.todoc.data.mask.response.ResStoreSaleResult

internal class MaskStoreRepositoryImpl(private val dataSource: MaskStoreDataSource) :
    MaskStoreRepository {
    override suspend fun getMaskStore(lat: Float, lng: Float, m: Int): ResStoreSaleResult
         = dataSource.getMaskStores(lat, lng, m)
}