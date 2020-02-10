package com.nexters.doctor24.todoc.data.detailed.response

import com.google.gson.annotations.SerializedName
import com.nexters.doctor24.todoc.data.detailed.response.EndTime
import com.nexters.doctor24.todoc.data.detailed.response.StartTime

data class Day(
    @SerializedName("dayType")
    val dayType: String,
    @SerializedName("startTime")
    val startTime: StartTime,
    @SerializedName("endTime")
    val endTime: EndTime
)