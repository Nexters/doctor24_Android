package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.data.mask.response.ResStoreSaleResult

internal interface MaskStoreRepository {
    suspend fun getMaskStore(lat: Float, lng: Float, m: Int): ResStoreSaleResult
}