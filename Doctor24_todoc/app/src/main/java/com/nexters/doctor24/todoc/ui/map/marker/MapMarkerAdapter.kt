package com.nexters.doctor24.todoc.ui.map.marker

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.ui.map.MarkerUIData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

internal class MapMarkerAdapter(val context: Context, private val naverMap: NaverMap) : MapMarkerProvider {

    interface MarkerClickListener {
        fun markerClick(marker: Marker)
    }

    private val ZINDEX_NORAML = 0
    private val ZINDEX_COUNT  = 100

    private val job = Job()     // 1
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    var listener : MarkerClickListener? = null

    val view = LayoutInflater.from(context).inflate(R.layout.view_marker_hospital, null)
    val markerView = view.findViewById<ImageView>(R.id.image_marker)
    val countView = view.findViewById<TextView>(R.id.text_count)

    private var markers: MutableList<Marker> = mutableListOf()
    private var newMarkerData: HashMap<LatLng, MarkerUIData> = hashMapOf()
    private var currentMarkerData: HashMap<LatLng, Marker> = hashMapOf()

    override fun drawMarker(data: List<MarkerUIData>, type: MarkerTypeEnum) {
        data.forEach {
            if(currentMarkerData[it.location] != null) {
                newMarkerData[it.location] = it

//                currentMarkerData[it.location] = it
            }
        }
       /* val removeData = currentMarkerData.filter {
            !newMarkerData.contains(it.key)
        }

        markers.forEachIndexed { index, it ->
            if(removeData[it.position] != null) it.map = null
            markers.removeAt(index)
        }*/

        markers.iterator().forEach { it.map = null }
        markers.clear()

        data.iterator().forEach {
            markers.add(
                if(it.count > 1) {
                    Marker().apply {
                        position = it.location
                        icon = drawCountMarkerIcon(type, it.count)
                        zIndex = ZINDEX_COUNT
                    }
                } else {
                    Marker().apply {
                        position = it.location
                        tag = it.name
                        icon = drawMarkerIcon(
                            type,
                            it.isEmergency,
                            it.isNight
                        )
                        zIndex = ZINDEX_NORAML
                        setOnClickListener {
                            listener?.markerClick(this)
                            updateMarker(this, type, true)
                            true
                        }
                    }
                })

        }

        markers.forEach { marker ->
            marker.map = naverMap
        }

    }

    override fun updateMarker(marker: Marker, type: MarkerTypeEnum, isSelected: Boolean) {
        if(isSelected) {
            marker.icon = drawSelectMarkerIcon(type)
        }
    }

    private fun drawSelectMarkerIcon(type: MarkerTypeEnum) : OverlayImage {
        return OverlayImage.fromResource(
            when(type) {
                MarkerTypeEnum.HOSPITAL -> R.drawable.ic_marker_hospital_select
                MarkerTypeEnum.PHARMACY -> R.drawable.ic_marker_pharmacy_select
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

    private fun setBitmap(view: View): Bitmap {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
    }

    fun onDestroy() {
        job.cancel()
    }
}