package com.nexters.doctor24.todoc.data.marker

import com.nexters.doctor24.todoc.R

/**
 * Created by jiyoung on 13/01/2020
 */
enum class MarkerTypeEnum(val type: String, val title: String) {
    HOSPITAL("hospital", "병원"),
    PHARMACY("pharmacy", "약국"),
    CORONA("corona", "코로나진료소"),
    SECURE("secure", "안심병원"),
    MASK("mask", "마스크");

    companion object {
        fun getMarkerType(type: String) : MarkerTypeEnum? = when(type) {
            HOSPITAL.type -> HOSPITAL
            PHARMACY.type -> PHARMACY
            CORONA.type -> CORONA
            SECURE.type -> SECURE
            MASK.type -> MASK
            else -> null
        }
    }
}

sealed class MedicalMarkerBundleEnum(val count: Int) {
    class Bundle(total : Int) : MedicalMarkerBundleEnum(total)
    class Piece() : MedicalMarkerBundleEnum(1)
}