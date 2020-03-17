package com.nexters.doctor24.todoc.data.mask

import com.nexters.doctor24.todoc.data.mask.response.ResStoreSaleResult

internal interface MaskStoreDataSource {
    suspend fun getMaskStores(lat: Float, lng: Float, m: Int): ResStoreSaleResult
}