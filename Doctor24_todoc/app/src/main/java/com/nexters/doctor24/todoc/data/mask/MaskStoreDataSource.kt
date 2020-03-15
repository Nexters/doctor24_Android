package com.nexters.doctor24.todoc.data.mask

import com.nexters.doctor24.todoc.data.mask.response.ResMaskStore

internal interface MaskStoreDataSource {
    suspend fun getMaskStores(page: Int, perPage: Int): ResMaskStore
}