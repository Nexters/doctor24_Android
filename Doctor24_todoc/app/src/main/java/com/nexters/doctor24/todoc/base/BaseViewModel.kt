package com.nexters.doctor24.todoc.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel

internal abstract class BaseViewModel : ViewModel() {

    @CallSuper
    override fun onCleared() {
        super.onCleared()
    }
}