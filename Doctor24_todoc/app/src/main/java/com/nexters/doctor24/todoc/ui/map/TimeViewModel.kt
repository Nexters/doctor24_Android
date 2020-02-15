package com.nexters.doctor24.todoc.ui.map

import android.util.Log
import android.widget.TimePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nexters.doctor24.todoc.base.BaseViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


internal class TimeViewModel() : BaseViewModel() {

    private val _nowTime = MutableLiveData<String>()
    val nowTime: LiveData<String> get() = _nowTime

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String> get() = _startTime
    private val _startStoredTime = MutableLiveData<String>("")
    val startStoredTime: LiveData<String> get() = _startStoredTime

    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String> get() = _endTime
    private val _endStoredTime = MutableLiveData<String>("")
    val endStoredTime: LiveData<String> get() = _endStoredTime

    private val _isOpen = MutableLiveData<Boolean>()
    val isOpen: LiveData<Boolean> get() = _isOpen

    private val _isSelected =
        MutableLiveData<Boolean>().apply { value = true }     //true - start, false - end
    val isSelected: LiveData<Boolean> get() = _isSelected
    private val _isPickerSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val isPickerSelected: LiveData<Boolean> get() = _isPickerSelected

    private val _isStartTimeChanged = MutableLiveData<Boolean>().apply { postValue(false) }
    val isStartTimeChanged: LiveData<Boolean> get() = _isStartTimeChanged
    private val _isEndTimeChanged = MutableLiveData<Boolean>().apply { postValue(false) }
    val isEndTimeChanged: LiveData<Boolean> get() = _isEndTimeChanged
    private val _isStartStoredTimeChanged = MutableLiveData<Boolean>().apply { postValue(false) }
    val isStartStoredTimeChanged: LiveData<Boolean> get() = _isStartStoredTimeChanged
    private val _isEndStoredTimeChanged = MutableLiveData<Boolean>().apply { postValue(false) }
    val isEndStoredTimeChanged: LiveData<Boolean> get() = _isEndStoredTimeChanged

    //3) 그냥 내릴 경우, 변경 전 시간으로 데이터 셋팅
    //4) 숫자 변동사항 있을경우, 확인버튼 활성화
    //5) 시간 setting 할 때 나올 수 있는 경우들 예외처리
    //      - 종료시간은 시작시간보다 느리게
    //      - 시작시간은 종료시간보다 빠르게

    init {
        getCurrentTime()
        storeTempTime()
    }

    fun isChangedStartTime(isChange : Boolean){
        _isStartTimeChanged.value = isChange
        Log.e("지금 start는!!", isChange.toString())
    }

    fun isChangedEndTime(isChange : Boolean){
        _isEndTimeChanged.value = isChange
        Log.e("지금 end는!!", isChange.toString())
    }

    fun isChangedStoredStartTime(isChange : Boolean){
        _isStartStoredTimeChanged.value = isChange
        Log.e("지금 start는!!", isChange.toString())
    }

    fun isChangedStoredEndTime(isChange : Boolean){
        _isEndStoredTimeChanged.value = isChange
        Log.e("지금 end는!!", isChange.toString())
    }

    fun storeTempTime() {
        _startStoredTime.value = _startTime.value
        _endStoredTime.value = _endTime.value

        Timber.e("startStore ${_startStoredTime.value}, startTime ${_startTime.value}")
        Timber.e("endStore ${_endStoredTime.value}, endTime ${_endTime.value}")

        _isOpen.value = false
    }

    fun setBottomSheetState(state: Int) {
        if (state == BottomSheetBehavior.STATE_COLLAPSED){
            _isOpen.value = false

            _isStartTimeChanged.value = false
            _isEndTimeChanged.value = false

        }
        else {
            _isOpen.value = true

            setTimeSelected(true)
        }
    }

    fun setChangeTime(view: TimePicker, isStart: Boolean) {
        Timber.e("setHour: ${view.hour} setMinute ${view.minute}")

        if (isStart) {
            _startTime.value = setTime(view.hour, view.minute)
        } else
            _endTime.value = setTime(view.hour, view.minute)
    }

    fun setTimeSelected(boolean: Boolean) {
        _isSelected.value = boolean
        _isSelected.postValue(boolean)
    }

    fun onClickTimeSetting(boolean: Boolean) {
        _isPickerSelected.value = boolean
        _isPickerSelected.postValue(boolean)
    }

    fun setInitialTime() {
        getCurrentTime()
        setTimeSelected(true)
    }

    private fun getCurrentTime() {
        val now = System.currentTimeMillis()
        val mDate = Date(now)

        val timeHour = SimpleDateFormat("HH").format(mDate).toInt()
        val timeMin = SimpleDateFormat("mm").format(mDate).toInt()

        _startTime.value = setTime(timeHour, timeMin)
        _endTime.value = setTime(timeHour + 1, timeMin)
    }

    private fun setAmPm(hour: Int): String {
        return if (hour >= 12)
            "오후"
        else
            "오전"
    }

    private fun setHour(hour: Int): String {
        return if (hour > 12)
            (hour - 12).toString()
        else
            "$hour"
    }

    private fun setMinute(min: Int): String {
        return if (min >= 10)
            "$min"
        else
            "0$min"
    }

    private fun setTime(hour: Int, minute: Int): String {
        return """${setAmPm(hour)} ${setHour(hour)}:${setMinute(minute)}"""
    }

    private fun validateEndTime(hour: Int, minute: Int) {
/*        if (hour > tp_time_picker.hour) {
            Toast.makeText(context, "시간 설정을 다시 해 주세요1", Toast.LENGTH_SHORT).show()
            return
        } else if ((hour == tp_time_picker.hour) && (minute > tp_time_picker.minute)) {
            Toast.makeText(context, "시간 설정을 다시 해 주세요2", Toast.LENGTH_SHORT).show()
            return
        }
        include_layout_set_time.visibility = View.VISIBLE
        include_layout_set_time_picker.visibility = View.GONE*/
    }


}