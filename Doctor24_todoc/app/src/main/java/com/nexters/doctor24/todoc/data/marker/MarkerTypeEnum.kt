package com.nexters.doctor24.todoc.data.marker

import com.nexters.doctor24.todoc.R

/**
 * Created by jiyoung on 13/01/2020
 */
enum class MarkerTypeEnum(val type: String, val title: String) {
    HOSPITAL("hospital", "병원"),
    PHARMACY("pharmacy", "약국"),
    MASK("mask", "마스크");

    companion object {
        fun getMarkerType(type: String) : MarkerTypeEnum? = when(type) {
            HOSPITAL.type -> HOSPITAL
            PHARMACY.type -> PHARMACY
            MASK.type -> MASK
            else -> null
        }
    }
}

enum class MaskTypeEnum(val type: String, val title: String) {
    MASK_PHARMACY("01", "약국"),
    MASK_POST("02", "우체국"),
    MASK_NH("03", "농협");

    companion object {
        fun getMaskType(type: String) : MaskTypeEnum? = when(type) {
            MASK_PHARMACY.type -> MASK_PHARMACY
            MASK_POST.type -> MASK_POST
            MASK_NH.type -> MASK_NH
            else -> null
        }
    }
}

enum class MaskStateEnum(val state: String, val title : String, val color : Int, val drawable: Int, val drawableMask : Int) {
    REMAIN_PLENTY("plenty","100개 이상", R.color.light_green, R.drawable.and_mask_small_enough_marker_withoutshadow, R.drawable.icon_mask_enough),
    REMAIN_SOME("some","30개~100개", R.color.orange, R.drawable.and_mask_small_nomal_marker_withoutshadow,R.drawable.icon_mask_normal),
    REMAIN_FEW("few","2개~29개", R.color.light_red, R.drawable.and_mask_small_shortage_marker_withoutshadow, R.drawable.icon_mask_shortage),
    REMAIN_EMPTY("empty","재고 없음", R.color.grey_2, R.drawable.and_mask_small_soldout_marker_withoutshadow,R.drawable.icon_mask_soldout),
    REMAIN_BREAK("break","중단", -1,-1,-1);

    companion object {
        fun getMaskState(state: String?) : MaskStateEnum = when(state) {
            REMAIN_PLENTY.state -> REMAIN_PLENTY
            REMAIN_SOME.state -> REMAIN_SOME
            REMAIN_FEW.state -> REMAIN_FEW
            REMAIN_EMPTY.state -> REMAIN_EMPTY
            else -> REMAIN_BREAK
        }
    }
}
