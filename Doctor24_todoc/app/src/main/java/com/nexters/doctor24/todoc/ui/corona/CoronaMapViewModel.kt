package com.nexters.doctor24.todoc.ui.corona

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.ui.map.NaverMapViewModel

internal class CoronaMapViewModel(private val sharedPreferences: SharedPreferences) : BaseViewModel() {

    private val _maskSelected = MutableLiveData<Boolean>().apply { value = true }
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

    private val _closeEvent = SingleLiveEvent<Unit>()
    val closeEvent : LiveData<Unit> get() = _closeEvent

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

    fun onClickClose() {
        _closeEvent.call()
    }
}