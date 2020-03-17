package com.nexters.doctor24.todoc.ui.corona

import android.content.SharedPreferences
import android.location.Location
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.api.error.ErrorResponse
import com.nexters.doctor24.todoc.api.error.handleError
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.base.Event
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.data.mask.response.ResStoreSaleResult
import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.repository.MaskStoreRepository
import com.nexters.doctor24.todoc.ui.map.MarkerUIData
import com.nexters.doctor24.todoc.ui.map.NaverMapViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketException
import java.util.ArrayList

internal class CoronaMapViewModel(private val dispatchers: DispatcherProvider,
                                  private val sharedPreferences: SharedPreferences,
                                  private val repo: MarkerRepository,
                                  private val maskRepo: MaskStoreRepository
) : BaseViewModel() {

    private val _markerList = MutableLiveData<List<ResMapLocation>>()
    private val _maskMarkerList = MutableLiveData<ResStoreSaleResult>()
    private val _hospitalMarkerDatas = MediatorLiveData<Event<ArrayList<MarkerUIData>>>().apply {
        addSource (_markerList) {
            val list = arrayListOf<MarkerUIData>()
            it.forEach { res ->
                list.add(
                    MarkerUIData(
                        location = LatLng(res.latitude, res.longitude),
                        count = res.total,
                        medicalType = res.facilities[0].medicalType,
                        isEmergency = res.facilities[0].emergency,
                        isNight = res.facilities[0].nightTimeServe,
                        name = res.facilities[0].placeName,
                        group = res.facilities
                    )
                )
            }
            postValue(Event(list))
        }

        addSource(_maskMarkerList) {
            val list = arrayListOf<MarkerUIData>()
            it.stores?.forEach { res ->
                list.add(
                    MarkerUIData(
                        location = LatLng(res.lat.toDouble(), res.lng.toDouble()),
                        count = 1,
                        medicalType = "mask",
                        isEmergency = false,
                        isNight = false,
                        name = res.name
                    )
                )
            }
            postValue(Event(list))
        }
    }
    private val _medicalListData = Transformations.map(_markerList) { response ->
        val list = arrayListOf<ResMapMarker>()
        response.flatMap { it.facilities }.forEach { list.add(it) }
        Event(list)
    }
    val medicalListData : LiveData<Event<ArrayList<ResMapMarker>>> get() = _medicalListData
    val hospitalMarkerDatas : LiveData<Event<ArrayList<MarkerUIData>>> get() = _hospitalMarkerDatas

    /*private val _maskMarkerDatas = Transformations.map(_maskMarkerList) {
        val list = arrayListOf<MarkerUIData>()
        it.stores?.forEach { res ->
            list.add(
                MarkerUIData(
                    location = LatLng(res.lat.toDouble(), res.lng.toDouble()),
                    count = 1,
                    medicalType = res.type,
                    isEmergency = false,
                    isNight = false,
                    name = res.name
                )
            )
        }
        Event(list)
    }
    val maskMarkerDatas : LiveData<Event<ArrayList<MarkerUIData>>> get() = _maskMarkerDatas*/

    private val _errorData = MutableLiveData<ErrorResponse>()
    val errorData: LiveData<ErrorResponse> get() = _errorData

    var currentMyLocation : LatLng? = null // 현재 사용자의 위치

    private val _maskSelected = MutableLiveData<Boolean>()
    val maskSelected : LiveData<Boolean> get() = _maskSelected

    private val _coronaSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val coronaSelected : LiveData<Boolean> get() = _coronaSelected

    private val _coronaSecureSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val coronaSecureSelected : LiveData<Boolean> get() = _coronaSecureSelected

    private val _coronaTagSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val coronaTagSelected : LiveData<Boolean> get() = _coronaTagSelected

    private val _refreshEvent = SingleLiveEvent<Unit>()
    val refreshEvent : LiveData<Unit> get() = _refreshEvent

    private val _showPopup = MutableLiveData<Boolean>()
    val showPopup : LiveData<Boolean> get() = _showPopup

    private val _medicalListEvent = SingleLiveEvent<Unit>()
    val medicalListEvent : LiveData<Unit> get() = _medicalListEvent

    private val _stockSwitchEvent = MutableLiveData<Boolean>().apply { postValue(false) }
    val stockSwitchEvent : LiveData<Boolean> get() = _stockSwitchEvent

    fun currentType() : MarkerTypeEnum = when {
        maskSelected.value == true -> {
            MarkerTypeEnum.MASK
        }
        coronaSelected.value == true -> {
            MarkerTypeEnum.CORONA
        }
        coronaSecureSelected.value == true -> {
            MarkerTypeEnum.SECURE
        }
        else -> MarkerTypeEnum.MASK
    }

    private val _closeEvent = SingleLiveEvent<Unit>()
    val closeEvent : LiveData<Unit> get() = _closeEvent

    fun reqCoronaMarker(center: LatLng) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getMarkers(
                    center = center,
                    type = currentType()
                )
                withContext(dispatchers.main()) {
                    _markerList.postValue(result)
                }

                Timber.d(result.toString())
            } catch (e:Exception) {
                when (e) {
                    is HttpException -> {
                        _errorData.postValue(handleError(e.code(), e.message()))
                    }
                    is SocketException -> {
                        _errorData.postValue(handleError(0, e.message ?: "SocketException"))
                    }
                    else -> _errorData.postValue(handleError(-100, "서버에서 데이터를 받아오지 못하였습니다."))
                }
            }
        }
    }

    fun reqMaskMarker(center: LatLng) {
        uiScope.launch(dispatchers.io()) {
            try {
                val lat = center.latitude.run {
                    if(this < 33) 33.0 else if (this > 43) 43.0 else this
                }
                val lng = center.longitude.run {
                    if(this < 124) 124.0 else if (this > 132) 132.0 else this
                }
                val result = maskRepo.getMaskStore(
                    lat = lat.toFloat(),
                    lng = lng.toFloat(),
                    m = 2000
                )
                withContext(dispatchers.main()) {
                    _maskMarkerList.postValue(result)
                }
                Timber.e("마스크 결과")
                Timber.e(result.toString())
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _errorData.postValue(handleError(e.code(), e.message()))
                    }
                    is SocketException -> {
                        _errorData.postValue(handleError(0, e.message ?: "SocketException"))
                    }
                    else -> _errorData.postValue(handleError(-100, "서버에서 데이터를 받아오지 못하였습니다."))
                }
            }
        }
    }

    fun onChangedMyLocation(location : Location) {
        val loc = LatLng(location.latitude, location.longitude)
        if(currentMyLocation == null || currentMyLocation != loc) {
            currentMyLocation = loc
        }
    }

    fun onClickMaskBtn(){
        _maskSelected.value = true
        _coronaSelected.value = false
        _coronaSecureSelected.value = false
        checkClickCoronaTag()
    }

    fun onClickCoronaBtn(){
        _maskSelected.value = false
        _coronaSelected.value = true
        _coronaSecureSelected.value = false
        checkClickCoronaTag()
    }

    fun onClickCoronaSecureBtn(){
        _maskSelected.value = false
        _coronaSelected.value = false
        _coronaSecureSelected.value = true
        checkClickCoronaTag()
        if(!sharedPreferences.getBoolean(NaverMapViewModel.KEY_PREF_CORONA_SECURE_POPUP, false)) {
            _showPopup.value = true
            sharedPreferences.edit { putBoolean(NaverMapViewModel.KEY_PREF_CORONA_SECURE_POPUP, true) }
        }
    }

    private fun checkClickCoronaTag(){
        _coronaTagSelected.value = _coronaSelected.value ?: false || _coronaSecureSelected.value ?: false
    }

    fun onClickList() {
        _medicalListEvent.call()
    }

    fun onClickRefresh() {
        _refreshEvent.call()
        /*_currentLocation.value?.let { location ->
            _currentZoom.value?.let { zoom ->
                if(_coronaTagSelected.value == true){
                    reqCoronaMarker(location)
                }
            }
        }*/
    }

    fun onClickStockBtn() {
        _stockSwitchEvent.value = !(stockSwitchEvent.value ?: false)
    }

    fun onClickClose() {
        _closeEvent.call()
    }
}