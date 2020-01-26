package com.nexters.doctor24.todoc.repository

import com.nexters.doctor24.todoc.api.error.ErrorHandler
import com.nexters.doctor24.todoc.api.error.ErrorResponse
import com.nexters.doctor24.todoc.api.error.NoDataException
import com.nexters.doctor24.todoc.api.error.NoResponseException
import com.nexters.doctor24.todoc.base.Error
import com.nexters.doctor24.todoc.base.Success
import com.nexters.doctor24.todoc.data.map.MapDataSource
import com.nexters.doctor24.todoc.ext.applyCommonSideEffects
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class MapRepositoryImpl(val dataSource: MapDataSource) : MapRepository {
    override fun getAddress(coords: String) = flow {
        emit(getAddressFromAPI(coords))
    }.applyCommonSideEffects()
    .catch {
        emit(Error(it))
    }

    private suspend fun getAddressFromAPI(coords: String) = dataSource.getAddress(coords)
        .run {
            if (isSuccessful && body() != null) {
                body()?.let {
                    Success(it)
                } ?: Error(NoDataException())
            } else {
                Error(
                    NoResponseException(
                        ErrorHandler.parseError<ErrorResponse>(errorBody())?.message
                    )
                )
            }
        }

}