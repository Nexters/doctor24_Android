package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.data.mask.response.ResMaskStore

internal interface MaskStoreRepository {
    suspend fun getMaskStore(page: Int, perPage: Int): ResMaskStore
}