package com.nexters.doctor24.todoc.ui.corona

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseActivity
import com.nexters.doctor24.todoc.base.EventObserver
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.databinding.ActivityCoronaMapBinding
import com.nexters.doctor24.todoc.ui.map.NaverMapFragment
import com.nexters.doctor24.todoc.ui.map.list.MedicalListActivity
import com.nexters.doctor24.todoc.ui.map.marker.MapMarkerManager
import com.nexters.doctor24.todoc.ui.map.popup.IntroPopUpDialog
import com.nexters.doctor24.todoc.util.isCurrentMapDarkMode
import com.nexters.doctor24.todoc.util.selectStyle
import com.nexters.doctor24.todoc.util.toDp
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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
    private lateinit var naverMap: NaverMap
    private lateinit var markerManager: MapMarkerManager
    private val listIntent by lazy { Intent(this, MedicalListActivity::class.java) }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fusedLocationSource = FusedLocationSource(this, NaverMapFragment.LOCATION_PERMISSION_REQUEST_CODE)

        binding.apply {
            vm = viewModel
        }
        binding.coronaMapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@CoronaActivity)
        }

        initObserve()
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
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
        viewModel.hospitalMarkerDatas.observe(this, EventObserver {
            if (it.isEmpty()) {
                val message = String.format(getString(R.string.medical_empty_corona))
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                markerManager.setMarker(arrayListOf())
                showRefresh()
            } else {
                markerManager.setMarker(it)
                hideRefresh()
                if(viewModel.coronaTagSelected.value == true) {
                    val cameraUpdate = CameraUpdate.fitBounds(markerManager.makeBounds(), 100).animate(
                        CameraAnimation.Easing)
                    naverMap.apply{
                        minZoom = MAP_ZOOM_LEVEL_CORONA
                        moveCamera(cameraUpdate)
                    }
                }
            }
        })

        viewModel.errorData.observe(this, Observer {
            Timber.e("Exception: ${it.code}")
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })

        viewModel.refreshEvent.observe(this, Observer {
            viewModel.reqCoronaMarker(naverMap.cameraPosition.target)
        })

        viewModel.closeEvent.observe(this, Observer {
            finish()
        })

        viewModel.medicalListData.observe(this, EventObserver {
            listIntent.apply {
                putExtra(MedicalListActivity.KEY_MEDI_LIST, it)
            }
        })

        viewModel.maskSelected.observe(this, Observer {
            binding.textBtnMask.selectStyle(it)
        })

        viewModel.coronaSelected.observe(this, Observer {
            binding.textBtnCorona.selectStyle(it)
            if (it) {
                if (::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
                viewModel.reqCoronaMarker(naverMap.cameraPosition.target)
            }
        })

        viewModel.coronaSecureSelected.observe(this, Observer {
            binding.textBtnSecure.selectStyle(it)
            if (it) {
                if (::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
                viewModel.reqCoronaMarker(naverMap.cameraPosition.target)
            }
        })

        viewModel.coronaTagSelected.observe(this, Observer {
            binding.buttonList.isVisible = it
        })

        viewModel.showPopup.observe(this, Observer {
            if(it) IntroPopUpDialog().show(supportFragmentManager, IntroPopUpDialog.TAG)
        })

    }

    private fun showRefresh() {
        if(!binding.btnRefresh.isVisible) {
            deSelectMarker()
            binding.btnRefresh.apply {
                isVisible = true
                startAnimation((AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_down)))
            }
        }
    }

    private fun hideRefresh() {
        if(!markerManager.isMarkerEmpty()) binding.btnRefresh.isVisible = false
    }

    private fun deSelectMarker() {
        markerManager.deSelectMarker()
        naverMap.setContentPadding(0, 0, 0, 0)
    }

    override fun onStart() {
        super.onStart()
        binding.coronaMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.coronaMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.coronaMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.coronaMapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.coronaMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.coronaMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.coronaMapView.onLowMemory()
    }
}