package com.nexters.doctor24.todoc.data.marker.response

import com.google.gson.annotations.SerializedName

internal data class ResMapMarker (
    @SerializedName("name")
    val placeName: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("medicalType") // hospital, pharmacy, animal
    val medicalType: String,
    @SerializedName("days")
    val days: List<OperatingDate>,
    @SerializedName("phone")
    val placePhone: String,
    @SerializedName("address")
    val placeAddress: String
)