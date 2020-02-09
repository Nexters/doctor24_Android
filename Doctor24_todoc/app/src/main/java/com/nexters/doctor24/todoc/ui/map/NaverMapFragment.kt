package com.nexters.doctor24.todoc.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.api.error.ErrorHandler
import com.nexters.doctor24.todoc.base.*
import com.nexters.doctor24.todoc.data.map.response.ResMapAddress
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.databinding.NavermapFragmentBinding
import com.nexters.doctor24.todoc.ui.map.category.CategoryAdapter
import com.nexters.doctor24.todoc.ui.map.marker.MapMarkerManager
import com.nexters.doctor24.todoc.ui.map.preview.PreviewFragment
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel


internal class NaverMapFragment : BaseFragment<NavermapFragmentBinding, NaverMapViewModel>(),
    OnMapReadyCallback, MapMarkerManager.MarkerClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val LAYOUT_SPAN_COUNT = 5
    }

    private lateinit var locationSource: FusedLocationSource
    override val layoutResId: Int
        get() = R.layout.navermap_fragment
    override val viewModel: NaverMapViewModel by viewModel()
    val viewModelTime: TimeViewModel by viewModel()

    private lateinit var naverMap: NaverMap
    private lateinit var markerManager: MapMarkerManager
    private val categoryAdapter : CategoryAdapter by lazy { CategoryAdapter(context!!) }
    private val bottomSheetCategory : BottomSheetDialog by lazy { BottomSheetDialog(context!!) }
    private val previewFragment : PreviewFragment by lazy {
        PreviewFragment().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.PreviewBottomSheetDialog)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            vm = viewModel
            vmTime = viewModelTime
        }

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@NaverMapFragment)
        }

        initView()
        initObserve()
        setBottomSheet()
    }

    fun setBottomSheet() {

        val bottomSheetBehavior = BottomSheetBehavior.from<View>(binding.mapBottom)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun initView() {
        val markerTypes = MarkerTypeEnum.values()
        binding.tab.apply {
            markerTypes.forEach {
                val tabView =
                    LayoutInflater.from(context).inflate(R.layout.item_tab, null) as TextView
                tabView.text = it.title
                tabView.setCompoundDrawablesRelativeWithIntrinsicBounds(it.icon, 0, 0, 0)
                addTab(newTab().setCustomView(tabView))
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val view = it.customView as TextView
                        view.setTextColor(resources.getColor(R.color.white))
                        viewModel.onChangeTab(markerTypes[it.position])
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val view = it.customView as TextView
                        view.setTextColor(resources.getColor(R.color.grey_2))
                    }
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val view = it.customView as TextView
                        view.setTextColor(resources.getColor(R.color.white))
                        viewModel.onChangeTab(markerTypes[it.position])
                    }
                }
            })
        }

        val view = layoutInflater.inflate(R.layout.bottom_category_dialog, null)
        val categoryView = view.findViewById<RecyclerView>(R.id.recycler_view_category)
        categoryView.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(context, LAYOUT_SPAN_COUNT)
        }
        bottomSheetCategory.setContentView(view)
    }

    private fun initObserve() {
        viewModel.hospitalMarkerDatas.observe(viewLifecycleOwner, EventObserver {
            if (it.isEmpty()) {
                Toast.makeText(context, "현재 운영중인 병원이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                markerManager.setMarker(it)
            }
        })

        viewModel.tabChangeEvent.observe(viewLifecycleOwner, Observer {
            viewModel.reqMarker(naverMap.cameraPosition.target, naverMap.cameraPosition.zoom)
        })

        viewModel.currentLocation.observe(viewLifecycleOwner, Observer {
//            viewModel.reqMarker(it, viewModel.currentZoom.value ?: 15.0)
            showRefresh()
        })

        viewModel.currentZoom.observe(viewLifecycleOwner, Observer { zoom ->
            /*viewModel.currentLocation.value?.let {
                viewModel.reqMarker(it, zoom)
            }*/
            showRefresh()
        })

        viewModel.categoryEvent.observe(viewLifecycleOwner, Observer {
            bottomSheetCategory.show()
        })

        viewModel.previewCloseEvent.observe(viewLifecycleOwner, Observer {
            deSelectMarker()
        })

        viewModel.refreshEvent.observe(viewLifecycleOwner, Observer {
            binding.btnRefresh.isVisible = false
        })
    }

    private fun showRefresh() {
        if(!binding.btnRefresh.isVisible && !previewFragment.isVisible) {
            binding.btnRefresh.apply {
                isVisible = true
                startAnimation((AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_down)))
            }
        }
    }


    override fun markerClick(marker: Marker) {
        deSelectMarker()
        markerManager.getMarkerItem(marker)?.run {
            if (markerManager.isEqualsSelectMarker(this)) return
            selectMarker(this)
        }
        moveMarkerBoundary(marker)
    }

    override fun markerBundleClick(marker: Marker) {

    }

    private fun moveMarkerBoundary(marker: Marker) {
        val cameraUpdate = CameraUpdate.scrollTo(marker.position).animate(CameraAnimation.Easing)
        naverMap.setContentPadding(0,0,0,550)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun selectMarker(markerItem: MarkerUIData?) {
        markerItem?.run {
            binding.btnRefresh.isVisible = false
            markerManager.selectMarker(this)
            previewFragment.show(childFragmentManager, previewFragment.tag)
            /*when {
                mapViewModel.currentState == ListState.Default -> mapViewModel.setListState(isMarkerClick = true)
            }

            geoJson?.run { mapViewModel.requestGeoAndListInfo(this) }

            val currentZoom = gMap?.cameraPosition?.zoom?.toInt() ?: 13
            val listType = getBottomCheckedType(this, mapListLayout.currentChecked())
            val searchTitle = complexName?.let { it } ?: name ?: ""

            if (!searchTitle.isNullOrEmpty()) mainSearchViewModel.setFilterSearchDesc(searchTitle)

            mapListLayout.setRdButton(listType)
            requestListAPI(listType, this, 1, currentZoom)

            if (!UserInfo.getInstance().filterMgr.isAptType) {
                mapViewModel.saveClusterClickHistory(this, currentZoom)
            }*/
        }
    }

    private fun deSelectMarker() {
        markerManager.deSelectMarker()
        naverMap.setContentPadding(0, 0, 0, 0)
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
        naverMap = map
        map.uiSettings.apply {
            isCompassEnabled = false
            isRotateGesturesEnabled = false
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
            isTiltGesturesEnabled = false
        }
        map.apply {
            locationSource = this@NaverMapFragment.locationSource
            locationTrackingMode = LocationTrackingMode.Follow
            isNightModeEnabled = true
            setBackgroundResource(NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK)
            mapType = NaverMap.MapType.Navi
            minZoom = 12.0
            maxZoom = 17.0
        }

        binding.tab.getTabAt(0)?.select()
        binding.buttonLocation.apply {
            setBackgroundResource(R.drawable.ic_current_location)
            this.map = map
        }
        markerManager = MapMarkerManager(context!!, naverMap).apply { listener = this@NaverMapFragment }

        map.setOnMapClickListener { pointF, latLng -> deSelectMarker() }
        map.addOnCameraIdleListener {
            viewModel.onChangedLocation(map.cameraPosition.target)
            viewModel.onChangedZoom(map.cameraPosition.zoom)
        }
    }

    private fun handleResponse(result: Result<ResMapAddress>) {
        when (result) {
            //comment this Success check if you are observing data from DB
            is Success<ResMapAddress> -> {
                result.data.addressData?.get(0)?.region?.area2?.areaName?.let {
                    Toast.makeText(context, "현재 구 주소 : $it", Toast.LENGTH_SHORT).show()
                }
            }
            is Error -> {
                view?.let { view ->
                    ErrorHandler.handleError(
                        view,
                        result
                    )
                }
            }
            is Progress -> {
                binding.progressBar.isVisible = result.isLoading
            }
        }
    }

}