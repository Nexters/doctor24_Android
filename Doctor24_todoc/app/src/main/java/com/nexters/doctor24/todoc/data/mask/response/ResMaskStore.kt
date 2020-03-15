package com.nexters.doctor24.todoc.data.mask.response


import com.google.gson.annotations.SerializedName

data class ResMaskStore(
    @SerializedName("count")
    val count: Int,
    @SerializedName("page")
    val page: String,
    @SerializedName("storeInfos")
    val storeInfos: List<StoreInfo>,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)