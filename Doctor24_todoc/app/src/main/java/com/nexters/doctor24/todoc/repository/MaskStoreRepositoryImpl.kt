package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.data.mask.MaskStoreDataSource
import com.nexters.doctor24.todoc.data.mask.response.ResMaskStore

internal class MaskStoreRepositoryImpl(private val dataSource: MaskStoreDataSource) :
    MaskStoreRepository {
    override suspend fun getMaskStore(page: Int, perPage: Int): ResMaskStore {
        return dataSource.getMaskStores(page, perPage)
    }
}