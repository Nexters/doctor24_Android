package com.nexters.doctor24.todoc.ext

import com.nexters.doctor24.todoc.base.Progress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import com.nexters.doctor24.todoc.base.Result
import kotlinx.coroutines.Job

fun <T : Any> Flow<Result<T>>.applyCommonSideEffects() =
    retryWhen { cause, attempt ->
        when {
            (cause is IOException) -> {
                delay(2000L * (attempt + 1))
                true
            }
            else -> {
                false
            }
        }
    }
        .onStart { emit(Progress(isLoading = true) as Result<Nothing>) }
        .onCompletion { emit(Progress(isLoading = false)  as Result<Nothing>) }

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}
