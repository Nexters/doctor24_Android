package com.nexters.doctor24.todoc.ui.map.preview

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.SingleLiveEvent


internal class PreviewViewModel : ViewModel() {
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