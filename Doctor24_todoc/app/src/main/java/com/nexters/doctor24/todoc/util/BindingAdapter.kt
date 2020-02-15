package com.nexters.doctor24.todoc.util

import android.graphics.Color
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nexters.doctor24.todoc.data.detailed.response.Day
import com.nexters.doctor24.todoc.data.marker.response.OperatingDate
import com.nexters.doctor24.todoc.ui.detailed.adapter.DayAdapter
import timber.log.Timber

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = View.VISIBLE
    } else
        view.visibility = View.GONE
}

@BindingAdapter("todayOpen")
internal fun setOpenDay(view: TextView, today: OperatingDate?) {

    var result = ""

    today?.let {

        val start = today.startTime.split(":")
        val end = today.endTime.split(":")

        result = "${setTime(start[0].toInt(), start[1].toInt())} ~ ${setTime(
            end[0].toInt(), end[1].toInt()
        )}"
    }
    view.text = result
}

@BindingAdapter("dayOpen")
fun setOpenDay(view: TextView, day: Day?) {

    var result = ""

    day?.let {
        it.toHHmmFormat()
    }

    view.text = result
}

fun Day.toHHmmFormat() : String {
    val start = this.startTime.split(":")
    val end = this.endTime.split(":")

    return "${start[0].toInt()}:${start[1]} ~ ${end[0].toInt()}:${end[1]}"
}

@BindingAdapter("dayType")
fun setDayType(view: TextView, day: String) {
    val dayType = hashMapOf<String, String>(
        "MONDAY" to "월요일",
        "TUESDAY" to "화요일",
        "WEDNESDAY" to "수요일",
        "THURSDAY" to "목요일",
        "FRIDAY" to "금요일",
        "SATURDAY" to "토요일",
        "SUNDAY" to "일요일",
        "HOLIDAY" to "공휴일"
    )
    view.text = dayType[day]
}

fun String.toWeekDayWord() : String {
    val dayType = hashMapOf<String, String>(
        "MONDAY" to "월",
        "TUESDAY" to "화",
        "WEDNESDAY" to "수",
        "THURSDAY" to "목",
        "FRIDAY" to "금"
    )
    return dayType[this] ?: ""
}


@BindingAdapter("categoryText")
fun setCategoryText(view: TextView, categories: List<String>?) {
/*
    categories?.let {
        for (i in categories){
        //진료과목과 일치하는 것만 출력 하도록 map
        }
    }*/
    view.text = categories?.joinToString(", ")
}

@BindingAdapter("setOpenData")
fun setOpenData(view: RecyclerView, openDatas: List<Day>?) {

    (view.adapter as DayAdapter).run {
        addItem(openDatas)
    }
}

@BindingAdapter("android:background")
fun setBackground(view: View, @DrawableRes resId: Int?) {
    resId?.let {
        view.setBackgroundResource(it)
    }
}

@BindingAdapter("android:selected_text")
fun setSelectedText(view: TextView, isVisible: Boolean) {
    if (isVisible) {
        view.setTextColor(Color.BLACK)
    } else
        view.setTextColor(Color.LTGRAY)
}

@BindingAdapter("android:set_picker_default")
fun setPickerDefault(view: TimePicker, time: String) {

    val ampm = time.split(" ")
    val setTime = ampm[1].split(":")
    var hour = setTime[0].toInt()

    if (ampm[0] == "오후") {
        hour += 12
    }

    view.hour = hour
    view.minute = setTime[1].toInt()
}


@BindingAdapter("android:set_default_text_color")
fun setDefaultTextColor(view: TextView, state: Boolean) {
    if(state){
        view.setTextColor(Color.BLACK)
    }else{
        view.setTextColor(Color.LTGRAY)
    }
}

fun setAmPm(hour: Int): String {
    return if (hour >= 12)
        "오후"
    else
        "오전"
}

fun setHour(hour: Int): String {
    return if (hour >= 12)
        (hour - 12).toString()
    else
        "$hour"
}

fun setMinute(min: Int): String {
    return if (min >= 10)
        "$min"
    else
        "0$min"
}

fun setTime(hour: Int, minute: Int): String {
    return """${setAmPm(hour)} ${setHour(hour)}:${setMinute(minute)}"""
}
