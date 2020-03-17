package com.nexters.doctor24.todoc.data.mask.response


import com.google.gson.annotations.SerializedName

data class StoreInfo(
    @SerializedName("addr")
    val addr: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)