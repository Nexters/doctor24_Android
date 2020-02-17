package com.nexters.doctor24.todoc.ui.map

import android.content.res.Resources
import android.widget.TimePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.util.isFasterThan
import com.nexters.doctor24.todoc.util.to24hourString
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

internal class TimeViewModel(val resource: Resources) : BaseViewModel() {

    private val _nowTime = MutableLiveData<String>()
    val nowTime: LiveData<String> get() = _nowTime
    private val _nowEndTime = MutableLiveData<String>()
    val nowEndTime: LiveData<String> get() = _nowEndTime

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
    private val _checkTimeLimit = MutableLiveData<String>()
    val checkTimeLimit: LiveData<String> get() = _checkTimeLimit

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

    private val _isReset = SingleLiveEvent<Unit>()
    val isReset: LiveData<Unit> get() = _isReset

    private val _clickReset = MutableLiveData<Boolean>().apply { postValue(false) }
    val clickReset: LiveData<Boolean> get() = _clickReset

    init {
        getCurrentTime()

        _startStoredTime.value = _startTime.value
        _endStoredTime.value = _endTime.value

        _isOpen.value = false
    }

    fun isChangedStartTime(isChange: Boolean) {
        _isStartTimeChanged.value = isChange
    }

    fun isChangedEndTime(isChange: Boolean) {
        _isEndTimeChanged.value = isChange
    }

    fun isChangedStoredStartTime(isChange: Boolean) {
        _isStartStoredTimeChanged.value = isChange
    }

    fun isChangedStoredEndTime(isChange: Boolean) {
        _isEndStoredTimeChanged.value = isChange
    }

    fun isChangeclickReset(isChange: Boolean) {
        _clickReset.value = isChange
    }

    fun storeTempTime() {
        if(checkTimeLimit() ==""){
            _startStoredTime.value = _startTime.value
            _endStoredTime.value = _endTime.value

            _isOpen.value = false
        }
    }

    fun setBottomSheetState(state: Int) {
        if (state == BottomSheetBehavior.STATE_COLLAPSED) {
            _isOpen.value = false

            _isStartTimeChanged.value = false
            _isEndTimeChanged.value = false

            Timber.e("startStore ${_startStoredTime.value}, startTime ${_startTime.value}")
            Timber.e("endStore ${_endStoredTime.value}, endTime ${_endTime.value}")

            if ((_startStoredTime.value != _startTime.value) || (_endStoredTime.value != _endTime.value)) {
                _startTime.value = _startStoredTime.value
                _endTime.value = _endStoredTime.value
            }

        } else {
            _isOpen.value = true

            setTimeSelected(true)
        }
    }

    fun setChangeTime(view: TimePicker, isStart: Boolean) {
        Timber.e("setHour: ${view.hour} setMinute ${view.minute}")

        if (isStart) {
            _startTime.value = setTime(view.hour, view.minute)
        } else {
            _endTime.value = setTime(view.hour, view.minute)
        }
    }

    private fun checkTimeLimit() : String {
        var message = ""

        _endTime.value?.to24hourString()?.let { end ->
            message = if (_startTime.value?.to24hourString()?.isFasterThan(end) == false) resource.getString(R.string.set_time_error_message) else ""
        }

        _checkTimeLimit.value = message
        return message
    }

    fun setTimeSelected(boolean: Boolean) {
        _isSelected.value = boolean
    }

    fun onClickTimeSetting(boolean: Boolean) {
        _isPickerSelected.value = boolean
    }

    fun setInitialTime() {
        getCurrentTime()
        setTimeSelected(true)

        if (_isOpen.value != true) {
            //api
            _isReset.call()
        }

        _clickReset.value = false
    }

    private fun getCurrentTime() {
        val now = System.currentTimeMillis()
        val mDate = Date(now)

        val timeHour = SimpleDateFormat("HH").format(mDate).toInt()
        val timeMin = SimpleDateFormat("mm").format(mDate).toInt()

        _nowTime.value = setTime(timeHour, timeMin)
        _nowEndTime.value = setTime(timeHour + 1, timeMin)

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

    fun setEndTimeLimit(){
        _endTime.value = "오후 11:59"
    }

    private fun validateEndTime() {
        _startTime.value?.to24hourString()?.let { start ->
            _endTime.value?.to24hourString()?.let { end ->
                val startTime = start.split(":")
                val endTime = end.split(":")

                if((startTime[1].toInt() >= 12) && (endTime[1].toInt() < 12)){
                    _endTime.value = "오후 11:59"
                }
            }
        }
    }
}