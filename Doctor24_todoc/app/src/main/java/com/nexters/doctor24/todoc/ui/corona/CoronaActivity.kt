package com.nexters.doctor24.todoc.ui.corona

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseActivity
import com.nexters.doctor24.todoc.base.EventObserver
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.databinding.ActivityCoronaMapBinding
import com.nexters.doctor24.todoc.ui.map.MarkerUIData
import com.nexters.doctor24.todoc.ui.map.NaverMapFragment
import com.nexters.doctor24.todoc.ui.map.list.MedicalListActivity
import com.nexters.doctor24.todoc.ui.map.marker.MapMarkerManager
import com.nexters.doctor24.todoc.ui.map.marker.group.GroupMarkerListDialog
import com.nexters.doctor24.todoc.ui.map.popup.IntroPopUpDialog
import com.nexters.doctor24.todoc.ui.map.preview.PreviewFragment
import com.nexters.doctor24.todoc.util.isCurrentMapDarkMode
import com.nexters.doctor24.todoc.util.selectStyle
import com.nexters.doctor24.todoc.util.toDp
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

internal class CoronaActivity : BaseActivity<ActivityCoronaMapBinding, CoronaMapViewModel>(),
    OnMapReadyCallback, MapMarkerManager.MarkerClickListener, PreviewFragment.PreviewListener {
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

    private var isMarkerSelected = false

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

        initView()
        initObserve()
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        map.uiSettings.apply {
            isCompassEnabled = false
            isRotateGesturesEnabled = true
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

        markerManager = MapMarkerManager(this, naverMap).apply { listener = this@CoronaActivity }

        map.addOnCameraIdleListener {
            showRefresh()
        }
    }

    override fun markerClick(marker: Marker) {
        deSelectMarker()
        marker.apply {
            isHideCollidedMarkers = true
            isHideCollidedSymbols = true
            isHideCollidedCaptions = true
        }
        marker.tag?.let{
            if((it as ArrayList<ResMapMarker>).isNotEmpty()) {
                val medicalData = Bundle().apply {
                    putParcelable(PreviewFragment.KEY_MEDICAL, it[0])
                    naverMap.cameraPosition.target?.let { loc->
                        Timber.d("MapApps - $loc")
                        putDoubleArray(PreviewFragment.KEY_MY_LOCATION, doubleArrayOf(loc.latitude, loc.longitude))
                    }
                }
                PreviewFragment().apply {
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.PreviewBottomSheetDialog)
                    arguments = medicalData
                    listener = this@CoronaActivity
                }.show(supportFragmentManager, PreviewFragment.TAG)
            }
        }
        markerManager.getMarkerItem(marker)?.run {
            if (markerManager.isEqualsSelectMarker(this)) return
            selectMarker(this)
        }
        moveMarkerBoundary(marker)
    }

    override fun markerBundleClick(marker: Marker) {
        deSelectMarker()

        Timber.d("marker tag : ${marker.tag}")
        marker.tag?.let {
            if ((it as ArrayList<ResMapMarker>).isNotEmpty()) {
                val groupData = Bundle().apply {
                    putParcelableArrayList(GroupMarkerListDialog.KEY_LIST, it)
                    naverMap.cameraPosition.target?.let { loc->
                        Timber.d("MapApps - $loc")
                        putDoubleArray(GroupMarkerListDialog.KEY_MY_LOCATION, doubleArrayOf(loc.latitude, loc.longitude))
                    }
                }
                GroupMarkerListDialog().apply {
                    arguments = groupData
                }.show(supportFragmentManager, GroupMarkerListDialog.TAG)
            }

        }
        moveMarkerBoundary(marker)
    }

    override fun onClosedPreview() {
        deSelectMarker()
    }

    private fun initView() {
        binding.buttonLocation.setOnClickListener {
            when(locationState) {
                LocationTrackingMode.Face -> {
                    locationState = LocationTrackingMode.NoFollow
                    binding.buttonLocation.setImageResource(R.drawable.ic_location_none)
                }
                LocationTrackingMode.NoFollow -> {
                    locationState = LocationTrackingMode.Follow
                    binding.buttonLocation.setImageResource(R.drawable.ic_location_local)
                }
                LocationTrackingMode.Follow -> {
                    locationState = LocationTrackingMode.Face
                    binding.buttonLocation.setImageResource(R.drawable.ic_location_follow)
                }
            }
            naverMap.locationTrackingMode = locationState
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
                val cameraUpdate = CameraUpdate.fitBounds(markerManager.makeBounds(), 100).animate(
                    CameraAnimation.Easing)
                naverMap.apply{
                    minZoom = MAP_ZOOM_LEVEL_CORONA
                    moveCamera(cameraUpdate)
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

        viewModel.medicalListEvent.observe(this, Observer {
            naverMap.cameraPosition.target?.let { loc->
                listIntent.apply{
                    putExtra(MedicalListActivity.KEY_MEDI_MY_LOCATION, doubleArrayOf(loc.latitude, loc.longitude))
                }
            }
            startActivity(listIntent)
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
        if(!binding.btnRefresh.isVisible && !isMarkerSelected) {
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

    private fun moveMarkerBoundary(marker: Marker) {
        val cameraUpdate = CameraUpdate.scrollTo(marker.position).animate(CameraAnimation.Easing)
        naverMap.setContentPadding(0, 0, 0, 270.toDp)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun selectMarker(markerItem: MarkerUIData?) {
        markerItem?.run {
            hideRefresh()
            isMarkerSelected = true
            markerManager.selectMarker(this)
        }
    }

    private fun deSelectMarker() {
        isMarkerSelected = false
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