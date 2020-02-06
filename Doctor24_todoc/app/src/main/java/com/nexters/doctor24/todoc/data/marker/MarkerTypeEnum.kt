package com.nexters.doctor24.todoc.data.marker

import com.nexters.doctor24.todoc.R

/**
 * Created by jiyoung on 13/01/2020
 */
enum class MarkerTypeEnum(val type: String, val title: String, val icon: Int) {
    HOSPITAL("hospital", "병원", R.drawable.selector_tab_hospital),
    PHARMACY("pharmacy", "약국", R.drawable.selector_tab_pharmacy)
//    ANIMAL("animal", "동물병원");
}