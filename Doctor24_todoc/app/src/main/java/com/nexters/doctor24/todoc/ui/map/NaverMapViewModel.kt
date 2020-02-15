package com.nexters.doctor24.todoc.ui.map

import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.base.Event
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.util.isCurrentMapDarkMode
import com.nexters.doctor24.todoc.util.to24hourString
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


internal class NaverMapViewModel(private val dispatchers: DispatcherProvider,
                                 private val sharedPreferences: SharedPreferences,
                                 private val repo: MarkerRepository) : BaseViewModel() {

    companion object {
        const val KEY_PREF_FILTER_CATEGORY = "KEY_PREF_FILTER_CATEGORY"
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
    }


    var currentMyLocation : LatLng? = null // 현재 사용자의 위치
    private val _currentLocation = MutableLiveData<LatLng>() // 현재 보고있는 지도의 center 좌표
    val currentLocation : LiveData<LatLng> get() = _currentLocation
    private val _currentZoom = MutableLiveData<Double>()
    val currentZoom : LiveData<Double> get() = _currentZoom
    private val _currentCategory = MutableLiveData<String?>().apply { postValue(null) }
    val currentCategory : LiveData<String?> get() = _currentCategory
    private val _mapDarkMode = MutableLiveData<Boolean>() // 지도 모드
    val mapDarkMode : LiveData<Boolean> get() = _mapDarkMode

    private val _markerList = MutableLiveData<List<ResMapLocation>>()
    private val _hospitalMarkerDatas = Transformations.map(_markerList) {
        val list = arrayListOf<MarkerUIData>()
        it.forEach {res->
            list.add(MarkerUIData(
                location = LatLng(res.latitude, res.longitude),
                count = res.total,
                medicalType = res.facilities[0].medicalType,
                isEmergency = res.facilities[0].emergency,
                isNight = res.facilities[0].nightTimeServe,
                name = res.facilities[0].placeName,
                group = res.facilities
            ))
        }
        Event(list)
    }
    private val _medicalListData = Transformations.map(_markerList) { response ->
        val list = arrayListOf<ResMapMarker>()
        response.flatMap { it.facilities }.forEach { list.add(it) }
        Event(list)
    }
    val medicalListData : LiveData<Event<ArrayList<ResMapMarker>>> get() = _medicalListData
    val hospitalMarkerDatas : LiveData<Event<ArrayList<MarkerUIData>>> get() = _hospitalMarkerDatas

    val selectedMarkerData = MutableLiveData<ResMapMarker>()

    private val _tabChangeEvent = MutableLiveData<MarkerTypeEnum>()
    val tabChangeEvent : LiveData<MarkerTypeEnum> get() = _tabChangeEvent

    private val _categoryShow = MutableLiveData<Boolean>()
    val categoryShow : LiveData<Boolean> get() = _categoryShow

    private val _categoryEvent = SingleLiveEvent<Int>()
    val categoryEvent : LiveData<Int> get() = _categoryEvent

    private val _previewCloseEvent = SingleLiveEvent<Unit>()
    val previewCloseEvent : LiveData<Unit> get() = _previewCloseEvent

    private val _refreshEvent = SingleLiveEvent<Unit>()
    val refreshEvent : LiveData<Unit> get() = _refreshEvent

    private val _dialogCloseEvent = SingleLiveEvent<Unit>()
    val dialogCloseEvent : LiveData<Unit> get() = _dialogCloseEvent

    fun reqMarker(center: LatLng, zoomLevel: Double, startTime : String?, endTime: String?) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getMarkers(
                    center = center,
                    type = tabChangeEvent.value ?: MarkerTypeEnum.HOSPITAL,
                    level = getRadiusLevel(zoomLevel),
                    category = if(tabChangeEvent.value == MarkerTypeEnum.PHARMACY) null else _currentCategory.value,
                    startTime = startTime,
                    endTime = endTime
                )
                withContext(dispatchers.main()) {
                    _markerList.value = result
                }
            } catch (e:Exception) {

            }
        }
    }

    fun reqMarkerWithCategory(category: String, start: String?, end:String?) {
        _currentCategory.value = category
        _currentLocation.value?.let { location ->
            _currentZoom.value?.let { zoom ->
                reqMarker(location, zoom, start, end)
            }
        }
    }

    fun setMapDarkMode() {
        _mapDarkMode.value = isCurrentMapDarkMode()
    }

    // 15 제일 좁은 영역 level 1 (0.5km 반경)
    // 14 ~ 13 좀더 확장된 영역 level 2 (1km)
    private fun getRadiusLevel(zoom: Double) : Int = when {
        zoom <= 14 -> 2
        zoom <= 15 -> 1
        else -> 1
    }

    fun onChangedMyLocation(location : Location) {
        val loc = LatLng(location.latitude, location.longitude)
        if(currentMyLocation == null || currentMyLocation != loc) {
            currentMyLocation = loc
        }
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
        if(type == MarkerTypeEnum.PHARMACY) {
            _currentCategory.value = null
            _categoryShow.value = false
        } else {
            _categoryShow.value = true
        }
    }

    fun onClickFilter() {
        _categoryEvent.value = sharedPreferences.getInt(KEY_PREF_FILTER_CATEGORY, 0)
    }

    fun onClosePreview() {
        _previewCloseEvent.call()
    }

    fun onClickRefresh(start:String, end:String) {
        _refreshEvent.call()
        _currentLocation.value?.let { location ->
            _currentZoom.value?.let { zoom ->
                reqMarker(location, zoom, start.to24hourString(), end.to24hourString())
            }
        }
    }

    fun onCloseDialog() {
        _dialogCloseEvent.call()
    }
}

internal data class MarkerUIData(
    val location: LatLng,
    val count: Int,
    val medicalType: String,
    val isEmergency: Boolean = false,
    val isNight: Boolean = false,
    val name: String,
    val group: ArrayList<ResMapMarker>
)