package com.nexters.doctor24.todoc.ui.map.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.SingleLiveEvent

internal class MedicalListViewModel : BaseViewModel() {

    private val _sortEvent = SingleLiveEvent<Unit>()
    val sortEvent : LiveData<Unit> get() = _sortEvent

    private val _closeEvent = SingleLiveEvent<Unit>()
    val closeEvent : LiveData<Unit> get() = _closeEvent

    fun onClickSort() {
        _sortEvent.call()
    }

    fun onClickClose() {
        _closeEvent.call()
    }
}