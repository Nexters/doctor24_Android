package com.nexters.doctor24.todoc.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

internal abstract class BaseViewModel : ViewModel() {

    private val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}