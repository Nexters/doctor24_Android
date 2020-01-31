package com.nexters.doctor24.todoc.data.marker.response

import com.google.gson.annotations.SerializedName

internal data class ResMapLocation (
    @SerializedName("latitude")
    val latitude: Double = 0.0,
    @SerializedName("longitude")
    val longitude: Double = 0.0,
    @SerializedName("facilities")
    val facilities: List<ResMapMarker> = emptyList(),
    @SerializedName("total")
    val total: Int = 0
)