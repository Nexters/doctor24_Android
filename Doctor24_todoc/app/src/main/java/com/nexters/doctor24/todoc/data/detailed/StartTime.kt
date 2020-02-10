package com.nexters.doctor24.todoc.data.detailed

import com.google.gson.annotations.SerializedName

data class StartTime(
    @SerializedName("hour")
    val hour: Int,
    @SerializedName("minute")
    val minute: Int,
    @SerializedName("nano")
    val nano: Int,
    @SerializedName("second")
    val second: Int
)