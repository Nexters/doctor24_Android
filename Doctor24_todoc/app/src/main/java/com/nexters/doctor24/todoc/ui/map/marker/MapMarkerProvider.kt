package com.nexters.doctor24.todoc.ui.map.marker

import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.ui.map.MarkerUIData

internal interface MapMarkerProvider {
    fun drawMarker(data: List<MarkerUIData>, type: MarkerTypeEnum)
    fun updateMarker()
}