package com.nexters.doctor24.todoc.data.map.response

import com.google.gson.annotations.SerializedName

open class ResBaseMap {
    @SerializedName("status")
    var status: ResMapStatus? = null
}

data class ResMapStatus(
    @SerializedName("code")
    val code: Int,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("message")
    val message: String? = null
)