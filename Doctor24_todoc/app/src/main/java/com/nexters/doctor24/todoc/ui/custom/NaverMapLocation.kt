package com.nexters.doctor24.todoc.ui.custom

import android.content.Context
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.widget.LocationButtonView

class NaverMapLocation(context: Context) : LocationButtonView(context) {
    override fun getMap(): NaverMap? {
        return super.getMap()
    }

    override fun setMap(map: NaverMap?) {
        super.setMap(map)
    }
}