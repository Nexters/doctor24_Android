package com.nexters.doctor24.todoc.ui.corona

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.ui.map.NaverMapViewModel

internal class CoronaMapViewModel(private val sharedPreferences: SharedPreferences) : BaseViewModel() {

    private val _coronaSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val coronaSelected : LiveData<Boolean> get() = _coronaSelected

    private val _coronaSecureSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val coronaSecureSelected : LiveData<Boolean> get() = _coronaSecureSelected

    private val _coronaTagSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val coronaTagSelected : LiveData<Boolean> get() = _coronaTagSelected

    private val _showPopup = MutableLiveData<Boolean>()
    val showPopup : LiveData<Boolean> get() = _showPopup

    private val _closeEvent = SingleLiveEvent<Unit>()
    val closeEvent : LiveData<Unit> get() = _closeEvent

    fun onClickMaskBtn(){
        val isSelect = _coronaSelected.value ?: true
        _coronaSelected.value = !isSelect
        _coronaSecureSelected.value = false
        checkClickCoronaTag()
    }

    fun onClickCoronaBtn(){
        val isSelect = _coronaSelected.value ?: true
        _coronaSelected.value = !isSelect
        _coronaSecureSelected.value = false
        checkClickCoronaTag()
    }

    fun onClickCoronaSecureBtn(){
        val isSelect = _coronaSecureSelected.value ?: true
        _coronaSelected.value = false
        _coronaSecureSelected.value = !isSelect
        checkClickCoronaTag()
        if(!sharedPreferences.getBoolean(NaverMapViewModel.KEY_PREF_CORONA_SECURE_POPUP, false)) {
            _showPopup.value = true
            sharedPreferences.edit { putBoolean(NaverMapViewModel.KEY_PREF_CORONA_SECURE_POPUP, true) }
        }
    }

    private fun checkClickCoronaTag(){
        _coronaTagSelected.value = _coronaSelected.value ?: false || _coronaSecureSelected.value ?: false
    }

    fun onClickClose() {
        _closeEvent.call()
    }
}