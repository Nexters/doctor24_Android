package com.nexters.doctor24.todoc.ui.corona

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseActivity
import com.nexters.doctor24.todoc.databinding.ActivityCoronaMapBinding
import com.nexters.doctor24.todoc.ui.map.NaverMapFragment
import com.nexters.doctor24.todoc.ui.map.popup.IntroPopUpDialog
import com.nexters.doctor24.todoc.util.isCurrentMapDarkMode
import com.nexters.doctor24.todoc.util.selectStyle
import com.nexters.doctor24.todoc.util.toDp
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class CoronaActivity : BaseActivity<ActivityCoronaMapBinding, CoronaMapViewModel>(),
    OnMapReadyCallback {
    override val layoutResId: Int
        get() = R.layout.activity_corona_map
    override val viewModel: CoronaMapViewModel by viewModel()

    companion object {
        private const val MAP_ZOOM_LEVEL_MIN = 12.0
        private const val MAP_ZOOM_LEVEL_MAX = 17.0
        private const val MAP_ZOOM_LEVEL_CORONA = 8.0
    }

    private lateinit var fusedLocationSource: FusedLocationSource
    private var locationState : LocationTrackingMode = LocationTrackingMode.Follow
    private lateinit var mapView: MapView

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fusedLocationSource = FusedLocationSource(this, NaverMapFragment.LOCATION_PERMISSION_REQUEST_CODE)

        binding.apply {
            vm = viewModel
        }
        mapView = binding.coronaMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initObserve()
    }

    override fun onMapReady(map: NaverMap) {
        map.uiSettings.apply {
            isCompassEnabled = false
            isRotateGesturesEnabled = false
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
            isTiltGesturesEnabled = false
            logoGravity = Gravity.TOP or Gravity.END
            setLogoMargin(0, 40.toDp, 24.toDp, 0)
        }
        map.apply {
            locationSource = fusedLocationSource
            locationTrackingMode = locationState
            isNightModeEnabled = isCurrentMapDarkMode()
            setBackgroundResource(NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK)
            mapType = NaverMap.MapType.Navi
            minZoom = MAP_ZOOM_LEVEL_CORONA
            maxZoom = MAP_ZOOM_LEVEL_MAX
        }
    }

    private fun initObserve() {
        viewModel.closeEvent.observe(this, Observer {
            finish()
        })

        viewModel.maskSelected.observe(this, Observer {
            binding.textBtnMask.selectStyle(it)
        })

        viewModel.coronaSelected.observe(this, Observer {
            binding.textBtnCorona.selectStyle(it)
            /*if (it) {
                if (::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
                viewModel.reqCoronaMarker(naverMap.cameraPosition.target)
            }*/
        })

        viewModel.coronaSecureSelected.observe(this, Observer {
            binding.textBtnSecure.selectStyle(it)
            /*if (it) {
                if (::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
                viewModel.reqCoronaMarker(naverMap.cameraPosition.target)
            }*/
        })

        viewModel.coronaTagSelected.observe(this, Observer {
            binding.buttonList.isVisible = it
        })

        viewModel.showPopup.observe(this, Observer {
            if(it) IntroPopUpDialog().show(supportFragmentManager, IntroPopUpDialog.TAG)
        })

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}