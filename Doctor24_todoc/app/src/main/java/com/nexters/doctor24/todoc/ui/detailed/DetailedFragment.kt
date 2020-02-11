package com.nexters.doctor24.todoc.ui.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseFragment
import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import com.nexters.doctor24.todoc.databinding.DetailedFragmentBinding
import com.nexters.doctor24.todoc.ui.detailed.adapter.DayAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class DetailedFragment : BaseFragment<DetailedFragmentBinding, DetailedViewModel>(),
    OnMapReadyCallback {

    private lateinit var naverMap: NaverMap

    override val layoutResId: Int
        get() = R.layout.detailed_fragment
    override val viewModel: DetailedViewModel by viewModel()
    private val dayAdapter by lazy { DayAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.reqDetailedInfo("hospital", "A1119764")

        binding.apply {
            vm = viewModel
        }

        binding.mvDetailedFragMapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@DetailedFragment)
        }

        setMovieRecyclerView()
    }

    override fun onMapReady(map: NaverMap) {
        setSelectedMarker(map, viewModel.detailedData)
    }

    private fun setMovieRecyclerView() {

        with(binding) {
            rvDetailedFragTime.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = dayAdapter
            }
        }
    }


    fun setSelectedMarker(map: NaverMap, selected: LiveData<DetailedInfoData>) {
        naverMap = map

        val marker = Marker()
        marker.position = LatLng(selected.value!!.latitude, selected.value!!.longitude)
        marker.map = naverMap
        marker.icon = drawSelectMarkerIcon(selected.value!!.medicalType)
    }

    private fun drawSelectMarkerIcon(type: String): OverlayImage {
        return OverlayImage.fromResource(
            when (type) {
                "hospital" -> R.drawable.ic_marker_hospital_select
                "pharmacy" -> R.drawable.ic_marker_pharmacy_select
                else -> R.drawable.ic_marker_hospital_select
            }
        )
    }

    inner class MarkerTagAdapter(private val selectedMarkerUIData: DetailedViewModel.SelectedMarkerUIData) :
        InfoWindow.ViewAdapter() {
        private val view = LayoutInflater.from(context).inflate(R.layout.item_marker_name, null)
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
}
