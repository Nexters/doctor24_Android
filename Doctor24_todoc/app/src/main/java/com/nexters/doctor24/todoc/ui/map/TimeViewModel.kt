package com.nexters.doctor24.todoc.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexters.doctor24.todoc.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*


internal class TimeViewModel() : BaseViewModel() {

    private val _startHour = MutableLiveData<String>()
    val startHour: LiveData<String> get() = _startHour
    private val _startMin = MutableLiveData<String>()
    val startMin: LiveData<String> get() = _startMin
    private val _startAmPm = MutableLiveData<String>()
    val startAmPm: LiveData<String> get() = _startAmPm

    private val _endHour = MutableLiveData<String>()
    val endHour: LiveData<String> get() = _endHour
    private val _endMin = MutableLiveData<String>()
    val endMin: LiveData<String> get() = _endMin
    private val _endAmPm = MutableLiveData<String>()
    val endAmPm: LiveData<String> get() = _endAmPm

    private val _isPickerSelected = MutableLiveData<Boolean>().apply { postValue(false) }
    val isPickerSelected: LiveData<Boolean> get() = _isPickerSelected

    init {
        getCurrentTime()
    }

    fun onClickTimeSetting(boolean: Boolean) {
        _isPickerSelected.value = boolean
    }

    fun getCurrentTime() {
        val now = System.currentTimeMillis()
        val mDate = Date(now)

        val timeHour = SimpleDateFormat("hh").format(mDate).toInt()
        val timeMin = SimpleDateFormat("mm").format(mDate).toInt()

        _startAmPm.value = setAmPm(timeHour)
        _startHour.value = setHour(timeHour)
        _startMin.value = setMinute(timeMin)
        Log.e("testtest00",startHour.value.toString()+startMin.value.toString())

        _endAmPm.value = setAmPm(timeHour)
        _endHour.value = setHour(timeHour + 1)
        _endMin.value = setMinute(timeMin)
    }

    private fun setAmPm(hour: Int): String {
        return if (hour >= 12)
            "오후"
        else
            "오전"
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