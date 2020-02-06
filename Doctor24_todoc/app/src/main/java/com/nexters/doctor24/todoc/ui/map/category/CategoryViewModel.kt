package com.nexters.doctor24.todoc.ui.map.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.SingleLiveEvent

internal class CategoryViewModel : BaseViewModel() {

    private val _categorySelectedEvent = MutableLiveData<Int>()
    val categorySelectedEvent : LiveData<Int> get() = _categorySelectedEvent

    private val _categoryCloseEvent = SingleLiveEvent<Unit>()
    val categoryCloseEvent : LiveData<Unit> get() = _categoryCloseEvent

    fun onSelectCategory(index : Int) {
        _categorySelectedEvent.value = index
    }

    fun onClickClose(){
        _categoryCloseEvent.call()
    }
}