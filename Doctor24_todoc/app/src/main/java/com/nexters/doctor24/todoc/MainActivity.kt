package com.nexters.doctor24.todoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private lateinit var locationSource : FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment?
            ?: run {
                val options = NaverMapOptions()
                    .nightModeEnabled(true)
                    .backgroundResource(NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK)
                    .mapType(NaverMap.MapType.Navi)
                    .minZoom(4.0)
                MapFragment.newInstance(options).also {
                    supportFragmentManager.beginTransaction().add(R.id.mapFragment, it).commit()
                }
            }
        mapFragment.getMapAsync { map ->
            map.uiSettings.apply {
                isCompassEnabled = false
                isRotateGesturesEnabled = false
                isZoomControlEnabled = false
                isLocationButtonEnabled = true
            }
            map.locationSource = locationSource
            map.locationTrackingMode = LocationTrackingMode.Face

            drawMarker(map)
        }
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

    private fun drawMarker(naverMap : NaverMap) {
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
