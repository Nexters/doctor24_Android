package com.nexters.doctor24.todoc.ui.detailed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.DispatcherProvider
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.data.detailed.response.Day
import com.nexters.doctor24.todoc.data.detailed.response.DetailedInfoData
import com.nexters.doctor24.todoc.repository.DetailedInfoRepository
import com.nexters.doctor24.todoc.ui.detailed.adapter.DayData
import com.nexters.doctor24.todoc.util.toHHmmFormat
import com.nexters.doctor24.todoc.util.toWeekDayWord
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class DetailedViewModel(
    private val dispatchers: DispatcherProvider,
    private val repo: DetailedInfoRepository
) : BaseViewModel() {

    private val _detailedData = MutableLiveData<DetailedInfoData>()
    val detailedData: LiveData<DetailedInfoData> get() = _detailedData

    private val _openDayData = MutableLiveData<ArrayList<DayData>>()
    val openDayData: LiveData<ArrayList<DayData>> get() = _openDayData

    private val _closeDetailed = SingleLiveEvent<Unit>()
    val closeDetailed : LiveData<Unit> get() = _closeDetailed

    fun reqDetailedInfo(type: String, facilityId: String) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getDetailedInfo(
                    type = type,
                    facilityId = facilityId
                )

                withContext(dispatchers.main()) {
                    _detailedData.value = result
                    val weekendList = mutableListOf<Day>()
                    val weekdayList = mutableListOf<Day>()
                    result.days.forEach {
                        when(it.dayType) {
                            "SATURDAY", "SUNDAY", "HOLIDAY" -> weekendList.add(it)
                            else -> weekdayList.add(it)
                        }
                    }
                    var list = setOperatingDay(weekdayList)
                    list.addAll(setWeekendDay(weekendList))

                    _openDayData.value = list
                }
            } catch (e: Exception) {

            }
        }
    }

    fun closeDetailed(){
        _closeDetailed.call()
    }

    private fun setWeekendDay(days: List<Day>): ArrayList<DayData> {

        var week = ""
        val openDayData = arrayListOf<DayData>()
        days.forEach {
            when (it.dayType) {
                "SATURDAY" -> week = "SATURDAY"
                "SUNDAY" -> week = "SUNDAY"
                "HOLIDAY" -> week = "HOLIDAY"
            }
            if (week != "")
                openDayData.add(DayData(it.toHHmmFormat(), week))
        }

        return openDayData
    }

    private fun setOperatingDay(days: List<Day>): ArrayList<DayData> {
        val hash = hashMapOf<String, ArrayList<String>>()
        days.asSequence().forEach {
            if (hash.containsKey(it.toHHmmFormat())) {
                hash[it.toHHmmFormat()]?.add(it.dayType.toWeekDayWord())
            } else if (it.dayType.toWeekDayWord() != "") {
                hash[it.toHHmmFormat()] = arrayListOf(it.dayType.toWeekDayWord())
            }
        }

        val result = arrayListOf<DayData>()
        hash.forEach { (s, arrayList) ->

            if (arrayList.count() > 1) {
                val weekday = arrayList.joinToString(separator = ",")
                result.add(DayData(s, weekday))
            } else {
                result.add(DayData(s, arrayList[0]))
            }
        }
        return result
    }

    internal data class SelectedMarkerUIData(
        val location: LatLng,
        val medicalType: String,
        val name: String
    )
}
