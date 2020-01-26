package com.nexters.doctor24.todoc.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.repository.MarkerRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList

internal class NaverMapViewModel(private val dispatchers: DispatcherProvider,
                                 private val repo: MarkerRepository) : BaseViewModel() {

    val tabList = listOf<MarkerTypeEnum>(MarkerTypeEnum.HOSPITAL, MarkerTypeEnum.PHARMACY, MarkerTypeEnum.ANIMAL)

    private val _markerList = MutableLiveData<List<ResMapMarker>>()
    private val _hospitalMarkerDatas = Transformations.map(_markerList) {
        val list = mutableListOf<MarkerUIData>()
        it.forEach {res->
            list.add(MarkerUIData(LatLng(res.latitude, res.longitude), res.placeName))
        }
        list.toImmutableList()
    }
    val hospitalMarkerDatas : LiveData<List<MarkerUIData>> get() = _hospitalMarkerDatas

    private val _tabChangeEvent = MutableLiveData<Int>().apply { postValue(0) }
    val tabChangeEvent : LiveData<Int> get() = _tabChangeEvent

    private val _bottomTitle = MutableLiveData<String>()
    val bottomTitle : LiveData<String> get() = _bottomTitle

    fun reqMarker(location: LatLng) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getMarkers(location.latitude.toString(), location.longitude.toString(), MarkerTypeEnum.HOSPITAL)
                withContext(dispatchers.main()) {
                    _markerList.value = result
                }
            } catch (e:Exception) {

            }
        }
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
    val name: String
)