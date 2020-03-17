package com.nexters.doctor24.todoc.data.mask.response

import com.google.gson.annotations.SerializedName

data class ResStoreSaleResult(
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("stores")
    val stores: List<StoreSale>? = emptyList()
)