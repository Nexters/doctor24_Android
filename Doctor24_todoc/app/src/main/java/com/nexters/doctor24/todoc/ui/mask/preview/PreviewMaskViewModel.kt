package com.nexters.doctor24.todoc.ui.mask.preview

import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.SingleLiveEvent

internal class PreviewMaskViewModel : ViewModel() {
    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation : LiveData<LatLng> get() = _currentLocation
    private val _medicalLocation = MutableLiveData<LatLng>()
    val medicalLocation : LiveData<LatLng> get() = _medicalLocation
    private val _showSelectAppDialog = SingleLiveEvent<Unit>()
    val showSelectAppDialog : LiveData<Unit> get() = _showSelectAppDialog

    fun setCurrentLocation(location: LatLng) {
        _currentLocation.value = location
    }

    fun onClickLoadMap() {
        _showSelectAppDialog.call()
    }
}