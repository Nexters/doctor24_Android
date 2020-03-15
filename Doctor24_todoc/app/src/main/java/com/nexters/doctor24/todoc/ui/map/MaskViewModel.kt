package com.nexters.doctor24.todoc.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.api.error.ErrorResponse
import com.nexters.doctor24.todoc.api.error.handleError
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.base.Event
import com.nexters.doctor24.todoc.data.mask.response.StoreInfo
import com.nexters.doctor24.todoc.repository.MaskStoreRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketException


internal class MaskViewModel(
    private val dispatchers: DispatcherProvider,
    private val repo: MaskStoreRepository
) : BaseViewModel() {

    private val _markerList = MutableLiveData<List<StoreInfo>>()
    private val _maskStoreMarkerDatas = Transformations.map(_markerList) {
        val list = arrayListOf<MaskMarkerUIData>()
        it.forEach { res ->
            list.add(
                MaskMarkerUIData(
                    location = LatLng(res.lat, res.lng),
                    medicalType = "mask",
                    name = res.name
                )
            )
        }
        Event(list)
    }
    private val _medicalListData = Transformations.map(_markerList) { response ->
        val list = arrayListOf<StoreInfo>()
        response.forEach { list.add(it) }
        Event(list)
    }

    private val _errorData = MutableLiveData<ErrorResponse>()
    val errorData: LiveData<ErrorResponse> get() = _errorData

    fun reqMarker(page: Int, perPage: Int) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getMaskStore(
                    page = page,
                    perPage = perPage
                )
                withContext(dispatchers.main()) {
                    _markerList.value = result.storeInfos
                }
                Timber.e("마스크 결과")
                Timber.e(result.toString())
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _errorData.postValue(handleError(e.code(), e.message()))
                    }
                    is SocketException -> {
                        _errorData.postValue(handleError(0, e.message ?: "SocketException"))
                    }
                    else -> _errorData.postValue(handleError(-100, "서버에서 데이터를 받아오지 못하였습니다."))
                }
            }
        }
    }
}

internal data class MaskMarkerUIData(
    val location: LatLng,
    val medicalType: String,
    val name: String
)