package com.nexters.doctor24.todoc.ui.detailed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import com.nexters.doctor24.todoc.repository.DetailedInfoRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DetailedViewModel(private val dispatchers: DispatcherProvider, private val repo: DetailedInfoRepository) : BaseViewModel() {

    private val _detailedData = MutableLiveData<DetailedInfoData>()
    val detailedData: LiveData<DetailedInfoData> get() = _detailedData

    fun reqDetailedInfo(type: String, facilityId: String) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getDetailedInfo(
                    type = type,
                    facilityId = facilityId
                )

                Log.e("detailedViewModel : ", result.toString())
                withContext(dispatchers.main()) {
                    _detailedData.value = result.body()
                }
            } catch (e: Exception) {

            }
        }
    }
}
