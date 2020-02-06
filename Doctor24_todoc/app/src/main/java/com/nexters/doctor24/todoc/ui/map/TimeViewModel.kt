package com.nexters.doctor24.todoc.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexters.doctor24.todoc.base.BaseViewModel

internal class TimeViewModel() : BaseViewModel() {

    private val _startHour = MutableLiveData<Int>()
    val startHour: LiveData<Int> get() = _startHour
    private val _startMin = MutableLiveData<Int>()
    val startMin: LiveData<Int> get() = _startMin

    private val _endHour = MutableLiveData<Int>()
    val endHour: LiveData<Int> get() = _endHour
    private val _endMin = MutableLiveData<Int>()
    val endMin: LiveData<Int> get() = _endMin

    private val _isPickerSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val isPickerSelected :LiveData<Boolean> get() = _isPickerSelected

    fun onClickTimeSetting(){
        _isPickerSelected.value = true
    }

    private fun setAmPm(hour: Int): String {
        return if (hour >= 12)
            "PM"
        else
            "AM"
    }

    private fun setHour(hour: Int): String {
        return if (hour >= 12)
            (hour - 12).toString()
        else
            hour.toString()
    }

    private fun setMinute(min: Int): String {
        return if (min >= 10)
            min.toString() + ""
        else
            "0$min"
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