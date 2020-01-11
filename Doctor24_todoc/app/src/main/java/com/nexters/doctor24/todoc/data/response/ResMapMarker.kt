package com.nexters.doctor24.todoc.data.response

import com.google.gson.annotations.SerializedName

internal data class ResMapMarker (
    @SerializedName("place_name")
    val placeName: String,
    @SerializedName("address_name")
    val addressName: String,
    val x: String,
    val y: String
)