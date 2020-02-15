package com.nexters.doctor24.todoc.ui.detailed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.data.detailed.response.Day
import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import com.nexters.doctor24.todoc.repository.DetailedInfoRepository
import com.nexters.doctor24.todoc.util.toHHmmFormat
import com.nexters.doctor24.todoc.util.toWeekDayWord
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DetailedViewModel(
    private val dispatchers: DispatcherProvider,
    private val repo: DetailedInfoRepository
) : BaseViewModel() {

    private val _detailedData = MutableLiveData<DetailedInfoData>()
    val detailedData: LiveData<DetailedInfoData> get() = _detailedData

    fun reqDetailedInfo(type: String, facilityId: String) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getDetailedInfo(
                    type = type,
                    facilityId = facilityId
                )

                withContext(dispatchers.main()) {
                    _detailedData.value = result
                    Log.e("detailedViewModel : ", result.toString())
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun setOperatingDay(days : List<Day>) {
        val hash = hashMapOf<String, ArrayList<String>>()
        days.asSequence().forEach {
            if (hash.containsKey(it.toHHmmFormat())) {
                hash[it.toHHmmFormat()]?.add(it.dayType.toWeekDayWord())
            } else {
                hash[it.toHHmmFormat()] = arrayListOf(it.dayType.toWeekDayWord())
            }
        }
        val result = arrayListOf<DayData>()
        hash.forEach { (s, arrayList) ->
            if(arrayList.count() > 1) {
                val weekday = arrayList.joinToString(separator = ",")
                result.add(DayData(s, weekday.substring(0, weekday.lastIndex)))
            } else {
                result.add(DayData(s, arrayList[0]))
            }
        }
    }

    data class DayData(
        val operatingHour : String,
        val weekday : String
    )

    internal data class SelectedMarkerUIData(
        val location: LatLng,
        val medicalType: String,
        val name: String
    )
}
