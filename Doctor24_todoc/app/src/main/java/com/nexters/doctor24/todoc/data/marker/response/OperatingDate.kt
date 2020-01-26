package com.nexters.doctor24.todoc.data.marker.response

import com.google.gson.annotations.SerializedName

/**
 * Created by jiyoung on 13/01/2020
 */
internal data class OperatingDate (
    @SerializedName("dayType") // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, HOLIDAY
    val dayType: String,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String
)