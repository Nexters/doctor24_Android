package com.nexters.doctor24.todoc.ui.map.marker

import com.naver.maps.map.overlay.Marker
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.ui.map.MarkerUIData

internal interface MapMarkerProvider {
    fun drawMarker(data: List<MarkerUIData>, type: MarkerTypeEnum)
    fun updateMarker(marker: Marker, type: MarkerTypeEnum, isSelected: Boolean)
}