package com.nexters.doctor24.todoc.ui.detailed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
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

    private val _naverMap = MutableLiveData<NaverMap>()
    val naverMap : LiveData<NaverMap> get() = _naverMap

    private val _closeDetailed = SingleLiveEvent<Unit>()
    val closeDetailed : LiveData<Unit> get() = _closeDetailed

    val corona = "corona"

    fun reqDetailedInfo(type: String, facilityId: String) {
        uiScope.launch(dispatchers.io()) {
            try {
                val result = repo.getDetailedInfo(
                    type = type,
                    facilityId = facilityId
                )

                withContext(dispatchers.main()) {
                    _detailedData.value = result
                    Timber.e(result.toString())
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

    fun setNaverMapView(map: NaverMap) {
        _naverMap.value = map
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

        val resultBundle = mutableListOf<DayData>()
        val resultSingle = mutableListOf<DayData>()
        val enum = WeekTypeEnum.values()
        hash.forEach { (s, arrayList) ->
            if (arrayList.count() > 1) {
                val weekday = arrayList.joinToString(separator = ",")
                if(weekday.contains("월") || weekday.contains("화")) {
                    resultBundle.add(0, DayData(s, weekday))
                } else {
                    resultBundle.add(DayData(s, weekday))
                }
            } else {
                resultSingle.add(DayData(s, "${arrayList[0]}요일"))
            }
        }
        resultSingle.sortBy { day->
            enum.find { it.title == day.weekday }
        }

        Timber.d("resultBundle : $resultBundle")
        Timber.d("resultSingle : $resultSingle")

        resultBundle.addAll(resultSingle)
        val array = arrayListOf<DayData>()
        resultBundle.forEach {
            array.add(it)
        }
        return array
    }

    enum class WeekTypeEnum(val index: Int, val dayType: String, val title: String) {
        EMPTY(-1, "", ""),
        MONDAY(0, "MONDAY", "월요일"),
        TUESDAY(1, "TUESDAY", "화요일"),
        WEDNESDAY(2, "WEDNESDAY", "수요일"),
        THURSDAY(3, "THURSDAY", "목요일"),
        FRIDAY(4, "FRIDAY", "금요일"),
        SATURDAY(5, "SATURDAY", "토요일"),
        SUNDAY(6, "SUNDAY", "일요일"),
        HOLIDAY(7, "HOLIDAY", "공휴일");
    }

    internal data class SelectedMarkerUIData(
        val location: LatLng,
        val medicalType: String,
        val name: String
    )
}
