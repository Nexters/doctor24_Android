package com.nexters.doctor24.todoc.ui.detailed

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseActivity
import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import com.nexters.doctor24.todoc.databinding.DetailedFragmentBinding
import com.nexters.doctor24.todoc.ui.detailed.adapter.DayAdapter
import com.nexters.doctor24.todoc.ui.findload.FindLoadDialog
import com.nexters.doctor24.todoc.ui.findload.FindLoadViewModel
import com.nexters.doctor24.todoc.util.isCurrentMapDarkMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

internal class DetailedActivity : BaseActivity<DetailedFragmentBinding, DetailedViewModel>(),
    OnMapReadyCallback {
    companion object {
        @JvmField
        val TAG: String = this::class.java.simpleName

        const val KEY_MEDICAL_TYPE: String = "KEY_MEDICAL_TYPE"
        const val KEY_MEDICAL_ID: String = "KEY_MEDICAL_ID"
        const val KEY_DISTANCE: String = "KEY_DISTANCE"
    }

    private lateinit var naverMap: NaverMap
    lateinit var marker: Marker

    override val layoutResId: Int
        get() = R.layout.detailed_fragment
    override val viewModel: DetailedViewModel by viewModel()
    private val dayAdapter by lazy { DayAdapter() }
    private val findLoadViewModel: FindLoadViewModel by viewModel()
    private val findLoadDialog: FindLoadDialog by lazy { FindLoadDialog(findLoadViewModel) }
    private var detailData : DetailedInfoData? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        overridePendingTransition(R.anim.act_slide_up, R.anim.no_animation)

        val type = intent.extras?.getString(KEY_MEDICAL_TYPE) ?: "hospital"
        val id = intent.extras?.getString(KEY_MEDICAL_ID)

        id?.let { viewModel.reqDetailedInfo(type, id) } ?: finish()

        binding.mvDetailedFragMapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@DetailedActivity)
        }

        binding.apply {
            vm = viewModel
        }

        initView()
        initObserve()
    }

    private fun initView() {

        binding.tvDetailedFragAddressDistance.text = intent.extras?.getString(KEY_DISTANCE) ?: ""
        binding.ivDetailedFragGotoMap.setOnClickListener {
            findLoadDialog.show(supportFragmentManager, FindLoadDialog.TAG)
        }

        binding.tvDetailedFragCallBtn.setOnClickListener {
            detailData?.phone.let {
                startActivity(Intent(Intent.ACTION_DIAL, ("tel:${it ?: ""}").toUri()))
            }
        }
    }

    private fun initObserve() {
        viewModel.detailedData.observe(this, Observer {
            if(::naverMap.isInitialized) setSelectedMarker(it)
            binding.data = it
            detailData = it
            findLoadViewModel.determineLocation = LatLng(it.latitude, it.longitude)
            findLoadViewModel.centerName = it.name
        })

        viewModel.closeDetailed.observe(this, Observer {
            finish()
        })

        viewModel.naverMap.observe(this, Observer {
            naverMap = it
            viewModel.detailedData.value?.let { setSelectedMarker(it) }
        })

        viewModel.openDayData.observe(this, Observer {
            with(binding) {
                rvDetailedFragTime.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = dayAdapter.apply { addItem(it) }
                }
            }
        })

        viewModel.errorData.observe(this, Observer {
            Timber.e("Exception: ${it.code}")
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onMapReady(map: NaverMap) {
        map.uiSettings.apply {
            isCompassEnabled = false
            isRotateGesturesEnabled = false
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
            isTiltGesturesEnabled = false
        }
        map.apply {
            map.isNightModeEnabled = isCurrentMapDarkMode()
            setBackgroundResource(NaverMap.DEFAULT_BACKGROUND_DRWABLE_DARK)
            mapType = NaverMap.MapType.Navi
        }
        viewModel.setNaverMapView(map)
    }

    private fun setSelectedMarker(selected: DetailedInfoData) {

        val marker = Marker().apply {
            position = LatLng(selected.latitude, selected.longitude)
            icon = drawSelectMarkerIcon(selected.medicalType)
            map = naverMap
        }

        InfoWindow().apply {
            adapter = MarkerTagAdapter(
                DetailedViewModel.SelectedMarkerUIData(
                    LatLng(selected.latitude, selected.longitude), selected.medicalType, selected.name))
            offsetY = -80
        }.open(marker, Align.Bottom)

        val cameraUpdate = CameraUpdate.scrollTo(marker.position)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun drawSelectMarkerIcon(type: String): OverlayImage {
        return OverlayImage.fromResource(
            when (type) {
                "hospital" -> R.drawable.ic_marker_hospital_select
                "pharmacy" -> R.drawable.ic_marker_pharmacy_select
                "corona" -> R.drawable.ic_marker_corona_select

                else -> R.drawable.ic_marker_hospital_select
            }
        )
    }

    inner class MarkerTagAdapter(private val selectedMarkerUIData: DetailedViewModel.SelectedMarkerUIData) :
        InfoWindow.ViewAdapter() {
        private val view =
            LayoutInflater.from(this@DetailedActivity).inflate(R.layout.item_marker_name, null)
        private val name = view.findViewById<TextView>(R.id.text_name)

        override fun getView(window: InfoWindow): View {
            name.text = selectedMarkerUIData.name
            return view
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mvDetailedFragMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mvDetailedFragMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mvDetailedFragMapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mvDetailedFragMapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mvDetailedFragMapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mvDetailedFragMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvDetailedFragMapView.onLowMemory()
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.no_animation, R.anim.act_slide_down)
    }
}
