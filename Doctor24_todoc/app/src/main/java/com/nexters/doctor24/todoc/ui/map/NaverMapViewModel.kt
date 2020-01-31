package com.nexters.doctor24.todoc.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.data.map.response.ResMapAddress
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.ext.cancelIfActive
import com.nexters.doctor24.todoc.repository.MarkerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import com.nexters.doctor24.todoc.base.Result
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation

internal class NaverMapViewModel(private val dispatchers: DispatcherProvider,
                                 private val repo: MarkerRepository) : BaseViewModel() {

    val tabList = listOf<MarkerTypeEnum>(MarkerTypeEnum.HOSPITAL, MarkerTypeEnum.PHARMACY, MarkerTypeEnum.ANIMAL)
    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation : LiveData<LatLng> get() = _currentLocation

    private val _markerList = MutableLiveData<List<ResMapLocation>>()
    private val _hospitalMarkerDatas = Transformations.map(_markerList) {
        val list = mutableListOf<MarkerUIData>()
        it.forEach {res->
            list.add(MarkerUIData(LatLng(res.latitude, res.longitude), res.total))
        }
        list.toImmutableList()
    }
    val hospitalMarkerDatas : LiveData<List<MarkerUIData>> get() = _hospitalMarkerDatas

    private val _mapAddressData = MutableLiveData<Result<ResMapAddress>>()
    val mapAddressData : LiveData<Result<ResMapAddress>> get() = _mapAddressData

    private val _boundsEvent = SingleLiveEvent<Unit>()
    val boundsEvent : LiveData<Unit> get() = _boundsEvent

    private val _tabChangeEvent = MutableLiveData<Int>().apply { postValue(0) }
    val tabChangeEvent : LiveData<Int> get() = _tabChangeEvent

    private val _bottomTitle = MutableLiveData<String>()
    val bottomTitle : LiveData<String> get() = _bottomTitle

    fun reqMarker(latitude: Double, longitude: Double) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getMarkers(latitude.toString(), longitude.toString(), MarkerTypeEnum.HOSPITAL)
                withContext(dispatchers.main()) {
                    _markerList.value = result
                }
            } catch (e:Exception) {

            }
        }
    }

    fun reqBounds(xLocation: LatLng, zLocation: LatLng) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getBounds(xLocation = xLocation, zLocation = zLocation, type = MarkerTypeEnum.HOSPITAL)
                withContext(dispatchers.main()) {
                    _markerList.value = result
                }
            } catch (e:Exception) {

            }
        }
    }

    fun onChangedLocation(location: LatLng) {
        if(_currentLocation.value != location) {
            _currentLocation.value = location
        }
    }

    fun onClickGetBounds() {
        _boundsEvent.call()
    }

    fun onChangeTab(position: Int) {
        _tabChangeEvent.value = position
    }

    fun onChangeBottomTitle(title: String) {
        _bottomTitle.value = title
    }
}

internal data class MarkerUIData(
    val location: LatLng,
    val count: Int
)