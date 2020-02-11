package com.nexters.doctor24.todoc.data.detailed.response

import com.google.gson.annotations.SerializedName

data class DetailedInfoData(
    @SerializedName("address")
    val address: String,
    @SerializedName("categories")
    val categories: List<String>,
    @SerializedName("days")
    val days: List<Day>,
    @SerializedName("emergency")
    val emergency: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("medicalType")
    val medicalType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nightTimeServe")
    val nightTimeServe: Boolean,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("today")
    val today: Today
)