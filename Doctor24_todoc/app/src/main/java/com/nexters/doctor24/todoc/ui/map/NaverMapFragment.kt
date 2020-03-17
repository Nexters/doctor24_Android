package com.nexters.doctor24.todoc.ui.map

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseFragment
import com.nexters.doctor24.todoc.base.EventObserver
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.databinding.NavermapFragmentBinding
import com.nexters.doctor24.todoc.ui.corona.CoronaActivity
import com.nexters.doctor24.todoc.ui.map.category.CategoryAdapter
import com.nexters.doctor24.todoc.ui.map.category.CategoryViewModel
import com.nexters.doctor24.todoc.ui.map.category.categoryItemList
import com.nexters.doctor24.todoc.ui.map.list.MedicalListActivity
import com.nexters.doctor24.todoc.ui.map.marker.MapMarkerManager
import com.nexters.doctor24.todoc.ui.map.marker.group.GroupMarkerListDialog
import com.nexters.doctor24.todoc.ui.map.popup.IntroPopUpDialog
import com.nexters.doctor24.todoc.ui.map.preview.PreviewFragment
import com.nexters.doctor24.todoc.ui.map.preview.PreviewViewModel
import com.nexters.doctor24.todoc.util.isCurrentMapDarkMode
import com.nexters.doctor24.todoc.util.to24hourString
import com.nexters.doctor24.todoc.util.toDp
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import timber.log.Timber


internal class NaverMapFragment : BaseFragment<NavermapFragmentBinding, NaverMapViewModel>(),
    OnMapReadyCallback, MapMarkerManager.MarkerClickListener, CategoryAdapter.CategoryListener, PreviewFragment.PreviewListener {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val LAYOUT_SPAN_COUNT = 5

        private const val MAP_ZOOM_LEVEL_MIN = 12.0
        private const val MAP_ZOOM_LEVEL_MAX = 17.0
        private const val MAP_ZOOM_LEVEL_CORONA = 8.0
    }

    private lateinit var locationSource: FusedLocationSource
    override val layoutResId: Int
        get() = R.layout.navermap_fragment
    override val viewModel: NaverMapViewModel by viewModel()
    val viewModelTime: TimeViewModel by viewModel()
    private val categoryViewModel : CategoryViewModel by viewModel()
    private val previewViewModel : PreviewViewModel by viewModel()
    private val maskViewModel : MaskViewModel by viewModel()

    private lateinit var naverMap: NaverMap
    private lateinit var markerManager: MapMarkerManager
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val bottomSheetCategory: BottomSheetDialog by lazy {
        BottomSheetDialog(context!!, R.style.PreviewBottomSheetDialog)
    }
    private val listIntent by lazy { Intent(context, MedicalListActivity::class.java) }
    private var locationState : LocationTrackingMode = LocationTrackingMode.Follow
    private var isSelected = false
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<View>

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

    private fun setBottomSheet() {

        bottomSheetBehavior = BottomSheetBehavior.from<View>(binding.mapBottom)
        var bgShape: GradientDrawable = binding.linearLayout.background as GradientDrawable

        bgShape.setColor(Color.WHITE)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int){
                when(newState) {
                    STATE_EXPANDED ->{
                        viewModelTime.setBottomSheetState(newState)
                    }
                    STATE_COLLAPSED ->{
                        viewModelTime.setBottomSheetState(newState)
                        if(viewModel.coronaTagSelected.value == true) showCoronaToast()
                    }
                }
                if (newState == STATE_COLLAPSED) {
                    bgShape.setColor(Color.WHITE)
                } else {
                    bgShape.setColor(Color.argb((100*2.55).toInt(),239, 242, 248))
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if(viewModel.coronaTagSelected.value == true) {
                    bottomSheetBehavior.state = STATE_COLLAPSED
                } else {
                    if (slideOffset > 0 && slideOffset < 1) {
                        bgShape.setColor(Color.argb((100*2.55).toInt(),239, 242, 248))
                        bgShape.alpha = floatToAlpha(slideOffset)
                    }

                    if(slideOffset == 0.toFloat())
                        bgShape.setColor(Color.WHITE)
                }
            }
        })
    }

    private fun floatToAlpha(float : Float) : Int {
        return (((float * 100).toInt()) * 2.55).toInt()
    }

    private fun initView() {

        Timber.e("yo1")
        viewModel.reqMaskMarker(1,500)
        Timber.e("yo2")

        binding.textTabHospital.setOnClickListener {
            if(viewModel.coronaTagSelected.value == false) {
                viewModel.onChangeTab(MarkerTypeEnum.HOSPITAL)
            } else {
                showCoronaToast()
            }
        }
        binding.textTabPharmacy.setOnClickListener {
            if(viewModel.coronaTagSelected.value == false) {
                viewModel.onChangeTab(MarkerTypeEnum.PHARMACY)
            } else {
                showCoronaToast()
            }
        }

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

        binding.buttonList.setOnClickListener {
            viewModel.currentMyLocation?.let { loc->
                listIntent.apply{
                    putExtra(MedicalListActivity.KEY_MEDI_MY_LOCATION, doubleArrayOf(loc.latitude, loc.longitude))
                }
            }
            startActivity(listIntent)
        }

        binding.linearLayout.setOnClickListener {
            bottomSheetBehavior.state = STATE_EXPANDED
        }

        binding.tvSetTime.setOnClickListener {
            bottomSheetBehavior.state = STATE_EXPANDED
        }

        binding.ivMapFragCloseBtn.setOnClickListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
        }

        initCategoryView()
    }

    private fun showCoronaToast() {
        val title = when {
            viewModel.coronaSelected.value == true -> {
                getString(R.string.map_corona_button)
            }
            viewModel.coronaSecureSelected.value == true -> {
                getString(R.string.map_secure_button)
            }
            else -> {
                getString(R.string.map_corona_button)
            }
        }
        val message = String.format(getString(R.string.map_corona_disable_message), title, title)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initCategoryView() {
        val view = layoutInflater.inflate(R.layout.bottom_category_dialog, null)
        val categoryView = view.findViewById<RecyclerView>(R.id.recycler_view_category)
        val refresh = view.findViewById<TextView>(R.id.text_category_reset)
        val btnClose = view.findViewById<ImageView>(R.id.button_close)
        categoryView.apply {
            adapter = categoryAdapter.apply {
                listener = this@NaverMapFragment
                submitList(categoryItemList)
            }
            layoutManager = GridLayoutManager(context, LAYOUT_SPAN_COUNT)
        }
        refresh.setOnClickListener { categoryViewModel.onClickRefresh() }
        btnClose.setOnClickListener { categoryViewModel.onClickClose() }
        bottomSheetCategory.setContentView(view)
    }

    private fun initObserve() {

        viewModelTime.startStoredTime.observe(viewLifecycleOwner, Observer {
            setResetVisibility()
        })

        viewModelTime.endStoredTime.observe(viewLifecycleOwner, Observer {
            setResetVisibility()
        })

        viewModelTime.checkTimeLimit.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModelTime.isReset.observe(viewLifecycleOwner, Observer {
            if(::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
            viewModel.reqMarker(naverMap.cameraPosition.target, naverMap.cameraPosition.zoom, viewModelTime.startTime.value?.to24hourString(), viewModelTime.endTime.value?.to24hourString())
        })

        viewModelTime.isOpen.observe(viewLifecycleOwner, Observer {
            if(it == false){
                bottomSheetBehavior.state = STATE_COLLAPSED
            }
            else
                bottomSheetBehavior.state = STATE_EXPANDED
        })

        viewModelTime.startTime.observe(viewLifecycleOwner, Observer{
            validateEndTime()

            if(it != viewModelTime.startStoredTime.value){
                viewModelTime.isChangedStartTime(true)
            }else{
                viewModelTime.isChangedStartTime(false)
            }
        })

        viewModelTime.endTime.observe(viewLifecycleOwner, Observer {
            validateEndTime()

            if (it != viewModelTime.endStoredTime.value) {
                viewModelTime.isChangedEndTime(true)
            } else {
                viewModelTime.isChangedEndTime(false)
            }
        })

        viewModel.hospitalMarkerDatas.observe(viewLifecycleOwner, EventObserver {
            if (it.isEmpty()) {
                val message = String.format(getString(R.string.medical_empty, viewModel.tabChangeEvent.value?.title ?: MarkerTypeEnum.HOSPITAL.title))
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                markerManager.setMarker(arrayListOf())
                showRefresh()
            } else {
                markerManager.setMarker(it)
                hideRefresh()
                if(viewModel.coronaTagSelected.value == true) {
                    val cameraUpdate = CameraUpdate.fitBounds(markerManager.makeBounds(), 100).animate(CameraAnimation.Easing)
                    naverMap.apply{
                        minZoom = MAP_ZOOM_LEVEL_CORONA
                        moveCamera(cameraUpdate)
                    }
                }
            }
        })

        viewModel.medicalListData.observe(viewLifecycleOwner, EventObserver {
            listIntent.apply {
                putExtra(MedicalListActivity.KEY_MEDI_LIST, it)
            }
        })

        viewModel.tabChangeEvent.observe(viewLifecycleOwner, Observer {
            if (it == MarkerTypeEnum.PHARMACY) {
                binding.itemTab.animate()
                    ?.translationX(binding.itemTab.width.minus(12.toDp).toFloat())
                    ?.setInterpolator(BounceInterpolator()
                    )?.setDuration(300)?.start()
            } else {
                binding.itemTab.animate()?.translationX(0f)
                    ?.setInterpolator(BounceInterpolator())?.setDuration(300)?.start()
            }
            binding.textTabHospital.isSelected = it == MarkerTypeEnum.HOSPITAL
            binding.textTabPharmacy.isSelected = it == MarkerTypeEnum.PHARMACY

            if(::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
            viewModel.reqMarker(naverMap.cameraPosition.target, naverMap.cameraPosition.zoom, viewModelTime.startTime.value?.to24hourString(), viewModelTime.endTime.value?.to24hourString())
        })

        viewModel.currentLocation.observe(viewLifecycleOwner, Observer {
            showRefresh()
        })

        viewModel.currentZoom.observe(viewLifecycleOwner, Observer {
            showRefresh()
        })

        viewModel.categoryEvent.observe(viewLifecycleOwner, Observer {
            bottomSheetCategory.show()
        })

        viewModel.refreshEvent.observe(viewLifecycleOwner, Observer {
            binding.btnRefresh.isVisible = false
        })

        viewModel.currentCategory.observe(viewLifecycleOwner, Observer {

            if(it == null || it.isEmpty()) {
                if(categoryAdapter.childViewList.isNotEmpty()){
                    categoryAdapter.childViewList[0].isChecked = true
                }
            }
        })

        viewModel.coronaSelected.observe(viewLifecycleOwner, Observer {
            binding.textBtnCorona.apply{
                isSelected = it
                setTypeface(null, if(it) Typeface.BOLD else Typeface.NORMAL)
            }
            if (it) {
                if (::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
                viewModel.reqCoronaMarker(naverMap.cameraPosition.target)
            }
        })

        viewModel.coronaSecureSelected.observe(viewLifecycleOwner, Observer {
            binding.textBtnSecure.apply{
                isSelected = it
                setTypeface(null, if(it) Typeface.BOLD else Typeface.NORMAL)
            }
            if (it) {
                if (::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
                viewModel.reqCoronaMarker(naverMap.cameraPosition.target)
            }
        })

        viewModel.showPopup.observe(viewLifecycleOwner, Observer {
            if(it) IntroPopUpDialog().show(childFragmentManager, IntroPopUpDialog.TAG)
        })

        viewModel.coronaTagSelected.observe(viewLifecycleOwner, Observer {
            if (it) {
                bottomSheetBehavior.peekHeight = 60.toDp
            } else {
                bottomSheetBehavior.peekHeight = 132.toDp
                bottomSheetBehavior.state = STATE_COLLAPSED

                if (::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
                if (::naverMap.isInitialized) {
                    naverMap.apply {
                        minZoom = MAP_ZOOM_LEVEL_MIN
                        moveCamera(CameraUpdate.zoomTo(15.0).animate(CameraAnimation.Easing))
                    }
                    viewModel.reqMarker(
                        naverMap.cameraPosition.target,
                        naverMap.cameraPosition.zoom,
                        viewModelTime.startTime.value?.to24hourString(),
                        viewModelTime.endTime.value?.to24hourString()
                    )
                }
            }
        })

        viewModel.coronaModeEvent.observe(viewLifecycleOwner, Observer {
            startActivity(Intent(context, CoronaActivity::class.java))
        })

        viewModel.errorData.observe(viewLifecycleOwner, Observer {
            Timber.e("Exception: ${it.code}")
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })

        categoryViewModel.currentSelectItem.observe(viewLifecycleOwner, Observer {
            viewModel.reqMarkerWithCategory(it, viewModelTime.startTime.value?.to24hourString(), viewModelTime.endTime.value?.to24hourString())
            bottomSheetCategory.dismiss()
        })

        categoryViewModel.refreshEvent.observe(viewLifecycleOwner, Observer {
            categoryAdapter.childViewList[0].isChecked = true
        })

        categoryViewModel.categoryCloseEvent.observe(viewLifecycleOwner, Observer {
            bottomSheetCategory.dismiss()
        })
    }

    private fun setResetVisibility(){
        if((viewModelTime.startStoredTime.value == viewModelTime.nowTime.value) && (viewModelTime.endStoredTime.value == viewModelTime.nowEndTime.value)){
            viewModelTime.isChangeclickReset(false)
        }else
            viewModelTime.isChangeclickReset(true)
    }

    private fun validateEndTime() {
        viewModelTime.isSelected.value?.let { selectStart ->
            if(selectStart) {
            } else {
                val startTime = viewModelTime.startTime.value?.to24hourString()?.split(":")
                val endTime = viewModelTime.endTime.value?.to24hourString()?.split(":")

                startTime?.let { start ->
                    endTime?.let { end ->

                        if((start[0].toInt()>=12) && (end[0].toInt()<12))
                            viewModelTime.setEndTimeLimit()
                    }
                }
            }
        }
    }

    private fun showRefresh() {
        if(!binding.btnRefresh.isVisible && !isSelected) {
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

    override fun onClickCategory(category: String) {
        categoryViewModel.onSelectCategory(category)
    }

    override fun onClosedPreview() {
        deSelectMarker()
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
                    viewModel.currentMyLocation?.let { loc->
                        Timber.d("MapApps - $loc")
                        putDoubleArray(PreviewFragment.KEY_MY_LOCATION, doubleArrayOf(loc.latitude, loc.longitude))
                    }
                }
                PreviewFragment().apply {
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.PreviewBottomSheetDialog)
                    arguments = medicalData
                    listener = this@NaverMapFragment
                }.show(childFragmentManager, PreviewFragment.TAG)
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
                    viewModel.currentMyLocation?.let { loc->
                        Timber.d("MapApps - $loc")
                        putDoubleArray(GroupMarkerListDialog.KEY_MY_LOCATION, doubleArrayOf(loc.latitude, loc.longitude))
                    }
                }
                GroupMarkerListDialog().apply {
                    arguments = groupData
                }.show(childFragmentManager, GroupMarkerListDialog.TAG)
            }

        }
        moveMarkerBoundary(marker)

    }

    private fun moveMarkerBoundary(marker: Marker) {
        val cameraUpdate = CameraUpdate.scrollTo(marker.position).animate(CameraAnimation.Easing)
        naverMap.setContentPadding(0, 0, 0, 270.toDp)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun selectMarker(markerItem: MarkerUIData?) {
        markerItem?.run {
            binding.btnRefresh.isVisible = false
            isSelected = true
            markerManager.selectMarker(this)
        }
    }

    private fun deSelectMarker() {
        isSelected = false
        markerManager.deSelectMarker()
        naverMap.setContentPadding(0, 0, 0, 0)
        bottomSheetBehavior.state = STATE_COLLAPSED
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
            isRotateGesturesEnabled = true
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
            isTiltGesturesEnabled = false
            logoGravity = Gravity.TOP or Gravity.END
            setLogoMargin(0, 138.toDp, 24.toDp, 0)
        }
        map.apply {
            locationSource = this@NaverMapFragment.locationSource
            locationTrackingMode = locationState
            isNightModeEnabled = isCurrentMapDarkMode()
            setBackgroundResource(NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK)
            mapType = NaverMap.MapType.Navi
            minZoom = MAP_ZOOM_LEVEL_MIN
            maxZoom = MAP_ZOOM_LEVEL_MAX
        }

        viewModel.onChangeTab(MarkerTypeEnum.HOSPITAL)

        markerManager =
            MapMarkerManager(context!!, naverMap).apply { listener = this@NaverMapFragment }

        map.setOnMapClickListener { pointF, latLng -> deSelectMarker() }
        map.addOnCameraIdleListener {
            viewModel.onChangedLocation(map.cameraPosition.target)
            viewModel.onChangedZoom(map.cameraPosition.zoom)
        }
        map.addOnLocationChangeListener {
            viewModel.onChangedMyLocation(it)
        }

        viewModelTime.startStoredTime.observe(viewLifecycleOwner, Observer {
            if(::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
            viewModel.reqMarker(naverMap.cameraPosition.target, naverMap.cameraPosition.zoom, viewModelTime.startTime.value?.to24hourString(), viewModelTime.endTime.value?.to24hourString())
        })

        viewModelTime.endStoredTime.observe(viewLifecycleOwner, Observer {
            if(::markerManager.isInitialized) markerManager.setMarker(arrayListOf())
            viewModel.reqMarker(naverMap.cameraPosition.target, naverMap.cameraPosition.zoom, viewModelTime.startTime.value?.to24hourString(), viewModelTime.endTime.value?.to24hourString())
        })
    }



    /*private fun handleResponse(result: Result<ResMapAddress>) {
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
    }*/

}