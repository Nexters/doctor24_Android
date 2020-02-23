package com.nexters.doctor24.todoc.data.detailed.response

import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("dayType")
    val dayType: String,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String
)