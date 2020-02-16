package com.nexters.doctor24.todoc.ui.map

import android.content.res.Resources
import android.widget.TimePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseViewModel
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import com.nexters.doctor24.todoc.util.isFasterThan
import com.nexters.doctor24.todoc.util.isLaterThan
import com.nexters.doctor24.todoc.util.to24hourString
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

internal class TimeViewModel(val resource: Resources) : BaseViewModel() {

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
    private val _checkTimeLimit = MutableLiveData<String>()
    val checkTimeLimit : LiveData<String> get() = _checkTimeLimit

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
    val isReset : LiveData<Unit> get() = _isReset

    //5) 시간 setting 할 때 나올 수 있는 경우들 예외처리
    //      - 종료시간은 시작시간보다 느리게
    //      - 시작시간은 종료시간보다 빠르게
    //      - 종료시간과 시작시간의 day차이 없도록(시작시간이 종료시간이 밤12시를 넘지 않도록)

    init {
        getCurrentTime()
        storeTempTime()
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

    fun storeTempTime() {
        _startStoredTime.value = _startTime.value
        _endStoredTime.value = _endTime.value



        _isOpen.value = false
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

            //
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
        checkTimeLimit(isStart)
    }

    private fun checkTimeLimit(isStart : Boolean){
        val message = if(isStart) {
            _endTime.value?.to24hourString()?.let { end ->
                if(_startTime.value?.to24hourString()?.isFasterThan(end) == false) resource.getString(R.string.start_time_error_message) else ""
            }
        } else {
            _startTime.value?.to24hourString()?.let { start ->
                if(_endTime.value?.to24hourString()?.isLaterThan(start) == false) resource.getString(R.string.end_time_error_message) else ""
            }
        }
        _checkTimeLimit.value = message ?: ""
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

        if(_isOpen.value != true){
            //api
            _isReset.call()
        }

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