package com.nexters.doctor24.todoc.ui.mask

import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.api.error.ErrorResponse
import com.nexters.doctor24.todoc.api.error.handleError
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.base.Event
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.data.marker.MaskStateEnum
import com.nexters.doctor24.todoc.data.marker.MaskStateEnum.Companion.getMaskState
import com.nexters.doctor24.todoc.data.mask.response.ResStoreSaleResult
import com.nexters.doctor24.todoc.repository.MaskStoreRepository
import com.nexters.doctor24.todoc.ui.map.MarkerUIData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketException
import java.util.*

internal class MaskMapViewModel(private val dispatchers: DispatcherProvider,
                                private val sharedPreferences: SharedPreferences,
                                private val maskRepo: MaskStoreRepository
) : BaseViewModel() {

    private val _maskMarkerList = MutableLiveData<ResStoreSaleResult>()
    private val _maskDisableList = MutableLiveData<ArrayList<MarkerUIData>>()
    private val _maskStockCount = MutableLiveData<Int>().apply { postValue(0) }
    private val _maskEnableMarkerList = Transformations.map(_maskMarkerList) {
        val list = arrayListOf<MarkerUIData>()
        val disableList = arrayListOf<MarkerUIData>()
        it.stores?.forEach { res ->
            list.add(
                MarkerUIData(
                    location = LatLng(res.lat.toDouble(), res.lng.toDouble()),
                    count = 1,
                    medicalType = "mask",
                    isEmergency = false,
                    isNight = false,
                    name = res.name,
                    maskType = res.type,
                    maskState = getMaskState(res.state)
                )
            )
            if (getMaskState(res.state) == MaskStateEnum.REMAIN_EMPTY || getMaskState(res.state) == MaskStateEnum.REMAIN_BREAK) {
                disableList.add(
                    MarkerUIData(
                        location = LatLng(res.lat.toDouble(), res.lng.toDouble()),
                        count = 1,
                        medicalType = "mask",
                        isEmergency = false,
                        isNight = false,
                        name = res.name,
                        maskType = res.type,
                        maskState = getMaskState(res.state)
                    )
                )
            }
        }
        _maskDisableList.value = disableList
        _maskStockCount.postValue(list.count() - disableList.count())
        Event(list)
    }

    val maskMarkerList : LiveData<Event<ArrayList<MarkerUIData>>> get() = _maskEnableMarkerList
    val maskDisableList : LiveData<ArrayList<MarkerUIData>> get() = _maskDisableList
    val maskStockCount : LiveData<Int> get() = _maskStockCount

    private val _errorData = MutableLiveData<ErrorResponse>()
    val errorData: LiveData<ErrorResponse> get() = _errorData

    var currentMyLocation : LatLng? = null // 현재 사용자의 위치

    private val _maskSelected = MutableLiveData<Boolean>()
    val maskSelected : LiveData<Boolean> get() = _maskSelected

    private val _refreshEvent = SingleLiveEvent<Unit>()
    val refreshEvent : LiveData<Unit> get() = _refreshEvent

    private val _showPopup = MutableLiveData<Boolean>()
    val showPopup : LiveData<Boolean> get() = _showPopup

    private val _stockSwitchEvent = MutableLiveData<Boolean>().apply { postValue(false) }
    val stockSwitchEvent : LiveData<Boolean> get() = _stockSwitchEvent

    private val _closeEvent = SingleLiveEvent<Unit>()
    val closeEvent : LiveData<Unit> get() = _closeEvent

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
                    m = 1500
                )
                withContext(dispatchers.main()) {
                    _maskMarkerList.postValue(result)
                }
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
    }

    fun onClickRefresh() {
        _refreshEvent.call()
    }

    fun onClickStockBtn() {
        _stockSwitchEvent.value = !(stockSwitchEvent.value ?: false)
    }

    fun onClickClose() {
        _closeEvent.call()
    }
}