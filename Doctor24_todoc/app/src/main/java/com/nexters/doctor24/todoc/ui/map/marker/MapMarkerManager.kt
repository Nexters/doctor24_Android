package com.nexters.doctor24.todoc.ui.map.marker

import android.content.Context
import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum.Companion.getMarkerType
import com.nexters.doctor24.todoc.data.marker.MedicalMarkerBundleEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.ui.custom.MapMarkerBounce
import com.nexters.doctor24.todoc.ui.custom.MapMarkerFade
import com.nexters.doctor24.todoc.ui.map.MarkerUIData
import java.util.HashMap

internal class MapMarkerManager(val context: Context, private val naverMap: NaverMap) {

    interface MarkerClickListener {
        fun markerClick(marker: Marker)
        fun markerBundleClick(marker: Marker)
    }

    var listener : MarkerClickListener? = null

    private val ZINDEX_DISABLE = -100
    private val ZINDEX_NORAML = 0
    private val ZINDEX_COUNT  = 100
    private val ZINDEX_SELECTED = 200

    private var selectMarkerItem: MarkerUIData? = null
    private val markerBounce by lazy { MapMarkerBounce() }
    private val mapMarkers = HashMap<MarkerUIData, Marker>()
    private val mapMarkerItems = HashMap<Marker, MarkerUIData>()

    private val view = LayoutInflater.from(context).inflate(R.layout.view_marker_hospital, null)
    private val markerView = view.findViewById<ImageView>(R.id.image_marker)
    private val countView = view.findViewById<TextView>(R.id.text_count)

    fun setMarker(markers: ArrayList<MarkerUIData>) {
        removeMarker(markers)
        addMarker(markers)
    }

    private fun removeMarker(markers: ArrayList<MarkerUIData>) {
        val removeMakers = ArrayList<MarkerUIData>()
        for (currentMarker in mapMarkers.keys) {
            if (checkExistMarker(currentMarker, markers).not()) {
                removeMakers.add(currentMarker)
            }
        }

        for (key in removeMakers) {
            mapMarkers[key]?.run {
                zIndex = ZINDEX_DISABLE
                map = null
                mapMarkers.remove(key)
                mapMarkerItems.remove(this)
            }
        }
    }

    private fun checkExistMarker(currentMaker: MarkerUIData, markers: ArrayList<MarkerUIData>): Boolean {
        for ((index, newMaker) in markers.withIndex()) {
            newMaker.apply {
                if(currentMaker.location == location) {
                    markers.removeAt(index)
                    return true
                }
            }
        }

        return false
    }

    private fun addMarker(markers: ArrayList<MarkerUIData>) {
        for (item in markers) {
            if (mapMarkers.containsKey(item).not()) {
                getAddMarkerDrawer(item)?.run {
                    mapMarkers[item] = this
                    mapMarkerItems[this] = item
                    MapMarkerFade.fadeIn(this, 700, MapMarkerFade.FADE_IN)

                    map = naverMap
                }
            }
        }
    }

    private fun getAddMarkerDrawer(marker: MarkerUIData): Marker? {
        marker.let {
            getMarkerType(it.medicalType)?.let { type ->
                return when {
                    it.count > 1 -> {
                        Marker().apply {
                            position = it.location
                            icon = drawCountMarkerIcon(type, it.count)
                            zIndex = ZINDEX_COUNT
                            setOnClickListener { overlay ->
                                overlay.tag = it.group
                                listener?.markerBundleClick(overlay as Marker)
                                true
                            }
                        }
                    }
                    else -> {
                        Marker().apply {
                            position = it.location
                            tag = it.name
                            icon = drawMarkerIcon(
                                type,
                                it.isEmergency,
                                it.isNight
                            )
                            zIndex = ZINDEX_NORAML
                            setOnClickListener { overlay ->
                                overlay.tag = it.group
                                listener?.markerClick(overlay as Marker)
                                true
                            }
                        }
                    }
                }
            } ?: return null
        }
    }

    private fun drawMarkerIcon(
        type: MarkerTypeEnum,
        isEmergency: Boolean,
        isNight: Boolean
    ): OverlayImage {
        return OverlayImage.fromResource(
            when (type) {
                MarkerTypeEnum.HOSPITAL -> when {
                    isEmergency -> R.drawable.ic_marker_hospital_emergency
                    isNight -> R.drawable.ic_marker_hospital_night
                    else -> R.drawable.ic_marker_hospital_normal
                }
                MarkerTypeEnum.PHARMACY -> R.drawable.ic_marker_pharmacy
            }
        )
    }

    private fun drawCountMarkerIcon(type: MarkerTypeEnum, total: Int): OverlayImage {
        markerView.setImageResource(when(type) {
            MarkerTypeEnum.HOSPITAL -> R.drawable.ic_marker_hospital_normal
            MarkerTypeEnum.PHARMACY -> R.drawable.ic_marker_pharmacy
        })
        countView.text = total.toString()
        return OverlayImage.fromView(view)
    }

    private fun drawSelectMarkerIcon(type: MarkerTypeEnum) : OverlayImage {
        return OverlayImage.fromResource(
            when(type) {
                MarkerTypeEnum.HOSPITAL -> R.drawable.ic_marker_hospital_select
                MarkerTypeEnum.PHARMACY -> R.drawable.ic_marker_pharmacy_select
            }
        )
    }

    fun selectMarker(markerItem: MarkerUIData) {
        val selectMarker = mapMarkers[markerItem]
        selectMarker?.run {
            selectMarkerItem = markerItem
            getMarkerType(markerItem.medicalType)?.let {
                selectMarker.icon = drawSelectMarkerIcon(it)
                InfoWindow().apply {
                    adapter = MarkerTagAdapter(markerItem)
                    offsetY = -80
                }.open(selectMarker, Align.Bottom)
            }
            zIndex = ZINDEX_SELECTED
            markerBounce.onMarkerClick(this, 1000L, 0.5f, 0.5f)
        }
    }

    fun deSelectMarker() {
        selectMarkerItem?.run {
            val marker = mapMarkers[this]
            marker?.let {
                getMarkerType(medicalType)?.let { type ->
                    it.icon = drawMarkerIcon(type, isEmergency, isNight)
                    it.infoWindow?.close()
                }
            }
            selectMarkerItem = null
        }
    }

    fun isEqualsSelectMarker(clickMarker: MarkerUIData): Boolean = (clickMarker == selectMarkerItem)

    fun getMarkerItem(marker: Marker): MarkerUIData? = mapMarkerItems[marker]

    fun getSelectMarkerItem(): MarkerUIData? = selectMarkerItem

    fun isMarkerEmpty() = mapMarkerItems.isEmpty()

    inner class MarkerTagAdapter(private val markerItem: MarkerUIData) : InfoWindow.ViewAdapter() {
        private val view = LayoutInflater.from(context).inflate(R.layout.item_marker_name, null)
        private val name = view.findViewById<TextView>(R.id.text_name)

        override fun getView(window: InfoWindow): View {
            name.text = markerItem.name
            return view
        }
    }
}