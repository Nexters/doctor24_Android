package com.nexters.doctor24.todoc.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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

    private val _startHour = MutableLiveData<Int>()
    val startHour : LiveData<Int> get() = _startHour
    private val _startMin = MutableLiveData<Int>()
    val startMin : LiveData<Int> get() = _startMin

    private val _endHour = MutableLiveData<Int>()
    val endHour : LiveData<Int> get() = _endHour
    private val _endMin = MutableLiveData<Int>()
    val endMin : LiveData<Int> get() = _endMin

    private val _currentLocation = MutableLiveData<LatLngBounds>()
    val currentLocation : LiveData<LatLngBounds> get() = _currentLocation

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

    fun reqBounds(bounds: LatLngBounds) {
        val xLoc = bounds.southWest
        val zLoc = bounds.northEast
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getBounds(xLocation = xLoc, zLocation = zLoc, type = tabChangeEvent.value ?: MarkerTypeEnum.HOSPITAL)
                withContext(dispatchers.main()) {
                    _markerList.value = result
                }
            } catch (e:Exception) {

            }
        }
    }

    fun onChangedLocation(location: LatLngBounds) {
        if(_currentLocation.value != location) {
            _currentLocation.value = location
        }
    }

    fun onChangeTab(type: MarkerTypeEnum) {
        _tabChangeEvent.value = type
    }


    private fun setAmPm(hour: Int): String {
        return if (hour >= 12)
            "PM"
        else
            "AM"
    }

    private fun setHour(hour: Int): String {
        return if (hour >= 12)
            (hour - 12).toString()
        else
            hour.toString()
    }

    private fun setMinute(min: Int): String {
        return if (min >= 10)
            min.toString() + ""
        else
            "0$min"
    }

}

internal data class MarkerUIData(
    val location: LatLng,
    val count: Int
)