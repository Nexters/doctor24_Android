package com.nexters.doctor24.todoc.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation
import com.nexters.doctor24.todoc.repository.MarkerRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList

internal class NaverMapViewModel(private val dispatchers: DispatcherProvider,
                                 private val repo: MarkerRepository) : BaseViewModel() {

    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation : LiveData<LatLng> get() = _currentLocation
    private val _currentZoom = MutableLiveData<Double>()
    val currentZoom : LiveData<Double> get() = _currentZoom

    private val _markerList = MutableLiveData<List<ResMapLocation>>()
    private val _hospitalMarkerDatas = Transformations.map(_markerList) {
        val list = mutableListOf<MarkerUIData>()
        it.forEach {res->
            list.add(MarkerUIData(LatLng(res.latitude, res.longitude), res.total))
        }
        list.toImmutableList()
    }
    val hospitalMarkerDatas : LiveData<List<MarkerUIData>> get() = _hospitalMarkerDatas

    private val _tabChangeEvent = MutableLiveData<MarkerTypeEnum>()
    val tabChangeEvent : LiveData<MarkerTypeEnum> get() = _tabChangeEvent

    /*fun reqMarker(location: LatLng) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getMarkers(location.latitude.toString(), location.longitude.toString(), MarkerTypeEnum.HOSPITAL)
                withContext(dispatchers.main()) {
                    _markerList.value = result
                }
            } catch (e:Exception) {

            }
        }
    }*/

    fun reqBounds(center: LatLng, zoomLevel: Double) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getMarkers(
                    center = center,
                    type = tabChangeEvent.value ?: MarkerTypeEnum.HOSPITAL,
                    level = getRadiusLevel(zoomLevel)
                )
                withContext(dispatchers.main()) {
                    _markerList.value = result
                }
            } catch (e:Exception) {

            }
        }
    }

    // 15 제일 좁은 영역 level 1 (0.5km 반경)
    // 14 ~ 13 좀더 확장된 영역 level 2 (1km)
    private fun getRadiusLevel(zoom: Double) : Int = when {
        zoom <= 14 -> 2
        zoom <= 15 -> 1
        else -> 1
    }

    fun onChangedLocation(location: LatLng) {
        if(_currentLocation.value != location) {
            _currentLocation.value = location
        }
    }

    fun onChangedZoom(zoomLevel: Double) {
        if(_currentZoom.value != zoomLevel) {
            _currentZoom.value = zoomLevel
        }
    }

    fun onChangeTab(type: MarkerTypeEnum) {
        _tabChangeEvent.value = type
    }
}

internal data class MarkerUIData(
    val location: LatLng,
    val count: Int
)