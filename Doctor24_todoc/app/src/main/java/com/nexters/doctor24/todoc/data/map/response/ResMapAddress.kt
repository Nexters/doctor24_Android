package com.nexters.doctor24.todoc.data.map.response

import com.google.gson.annotations.SerializedName

data class ResMapAddress(
    @SerializedName("results")
    var addressData : List<ResAddressData>?
) : ResBaseMap()

data class ResAddressData(
    @SerializedName("name")
    val typeName: String,
    @SerializedName("region")
    val region: ResRegion
)

data class ResRegion(
    @SerializedName("area0")
    val area0: ResArea? = null,
    @SerializedName("area1")
    val area1: ResArea? = null,
    @SerializedName("area2")
    val area2: ResArea? = null,
    @SerializedName("area3")
    val area3: ResArea? = null,
    @SerializedName("area4")
    val area4: ResArea? = null
)

data class ResArea(
    @SerializedName("name")
    val areaName: String,
    @SerializedName("coords")
    val coords: ResCoords?
)

data class ResCoords(
    @SerializedName("center")
    val coordsCenter: ResCoordsCenter
)

data class ResCoordsCenter(
    @SerializedName("crs")
    val crs: String,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)