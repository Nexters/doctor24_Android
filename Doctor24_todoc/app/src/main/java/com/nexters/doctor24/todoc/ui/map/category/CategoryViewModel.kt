package com.nexters.doctor24.todoc.ui.map.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.SingleLiveEvent

internal class CategoryViewModel : BaseViewModel() {

    private val _currentSelectItem = MutableLiveData<String>()
    val currentSelectItem : LiveData<String> get() = _currentSelectItem

    private val _refreshEvent = SingleLiveEvent<Unit>()
    val refreshEvent : LiveData<Unit> get() = _refreshEvent

    private val _categoryCloseEvent = SingleLiveEvent<Unit>()
    val categoryCloseEvent : LiveData<Unit> get() = _categoryCloseEvent

    fun onSelectCategory(category : String) {
        _currentSelectItem.value = category
    }

    fun onClickRefresh() {
        _refreshEvent.call()
    }

    fun onClickClose(){
        _categoryCloseEvent.call()
    }
}