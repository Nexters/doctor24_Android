package com.nexters.doctor24.todoc.ui.map

import android.os.Bundle
import android.view.View
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseFragment
import com.nexters.doctor24.todoc.databinding.NavermapFragmentBinding
import kotlinx.android.synthetic.main.layout_set_time.*
import kotlinx.android.synthetic.main.layout_time_picker.*
import kotlinx.android.synthetic.main.navermap_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


internal class NaverMapFragment : BaseFragment<NavermapFragmentBinding, NaverMapViewModel>(),
    OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private lateinit var locationSource: FusedLocationSource
    override val layoutResId: Int
        get() = R.layout.navermap_fragment
    override val viewModel: NaverMapViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@NaverMapFragment)
        }

        tab.addTab(tab.newTab().apply { text = "병원" })
        tab.addTab(tab.newTab().apply { text = "약국" })
        tab.addTab(tab.newTab().apply { text = "동물병원" })

        setStartTime()
        setEndTime()
    }

    private fun setEndTime(){
        ll_layout_set_end_time.setOnClickListener {
            include_set_time_picker.visibility = View.VISIBLE
            include_layout_set_time.visibility = View.GONE

            btn_set_end_time.visibility = View.VISIBLE
            btn_set_start_time.visibility = View.GONE
        }

        btn_set_end_time.setOnClickListener {
            tv_end_time.text = "${setHour(tp_time_picker.hour)}:${setMinute(tp_time_picker.minute)}"
            tv_end_time_ampm.text = getAmPm(tp_time_picker.hour)

            include_layout_set_time.visibility = View.VISIBLE
            include_set_time_picker.visibility = View.GONE
        }

    }

    private fun setStartTime(){
        ll_layout_set_start_time.setOnClickListener {
            include_set_time_picker.visibility = View.VISIBLE
            include_layout_set_time.visibility = View.GONE

            btn_set_start_time.visibility = View.VISIBLE
            btn_set_end_time.visibility = View.GONE
        }

        btn_set_start_time.setOnClickListener {
            tv_start_time.text = "${setHour(tp_time_picker.hour)}:${setMinute(tp_time_picker.minute)}"
            tv_start_time_ampm.text = getAmPm(tp_time_picker.hour)

            include_layout_set_time.visibility = View.VISIBLE
            include_set_time_picker.visibility = View.GONE
        }

    }

    private fun getAmPm(hour: Int): String {
        return if (hour >= 12)
            "AM"
        else
            "PM"
    }

    private fun setHour(hour: Int): Int {
        return if (hour >= 12)
            (hour - 12)
        else
            hour
    }

    private fun setMinute(min: Int): String {
        return if (min >= 10)
            min.toString() + ""
        else
            "0$min"
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(map: NaverMap) {
        map.uiSettings.apply {
            isCompassEnabled = false
            isRotateGesturesEnabled = false
            isZoomControlEnabled = false
            isLocationButtonEnabled = true
        }
        map.apply {
            locationSource = this@NaverMapFragment.locationSource
            locationTrackingMode = LocationTrackingMode.Face
            isNightModeEnabled = true
            setBackgroundResource(NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK)
            mapType = NaverMap.MapType.Navi
            minZoom = 4.0
        }


        drawMarker(map)
    }

    private fun drawMarker(naverMap: NaverMap) {
        arrayOf(
            LatLng(37.5666102, 126.9783881),
            LatLng(37.57000, 126.97618),
            LatLng(37.56138, 126.97970)
        ).map { coord ->
            Marker().apply {
                position = coord
                icon = MarkerIcons.GRAY
                map = naverMap
            }
        }
    }
}