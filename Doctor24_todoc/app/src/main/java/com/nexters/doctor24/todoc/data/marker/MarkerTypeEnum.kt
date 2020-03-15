package com.nexters.doctor24.todoc.data.marker

import com.nexters.doctor24.todoc.R

/**
 * Created by jiyoung on 13/01/2020
 */
enum class MarkerTypeEnum(val type: String, val title: String, val icon: Int) {
    HOSPITAL("hospital", "병원", R.drawable.selector_tab_hospital),
    PHARMACY("pharmacy", "약국", R.drawable.selector_tab_pharmacy),
    CORONA("corona", "코로나진료소", R.drawable.selector_tab_hospital),
    SECURE("secure", "안심병원", R.drawable.selector_tab_hospital);

    companion object {
        fun getMarkerType(type: String) : MarkerTypeEnum? = when(type) {
            HOSPITAL.type -> HOSPITAL
            PHARMACY.type -> PHARMACY
            CORONA.type -> CORONA
            SECURE.type -> SECURE
            else -> null
        }
    }
}

sealed class MedicalMarkerBundleEnum(val count: Int) {
    class Bundle(total : Int) : MedicalMarkerBundleEnum(total)
    class Piece() : MedicalMarkerBundleEnum(1)
}