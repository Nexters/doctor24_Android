package com.nexters.doctor24.todoc.util

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nexters.doctor24.todoc.data.detailed.response.Day
import com.nexters.doctor24.todoc.data.detailed.response.Today
import com.nexters.doctor24.todoc.ui.detailed.adapter.DayAdapter

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = View.VISIBLE
    } else
        view.visibility = View.GONE
}

@BindingAdapter("todayOpen")
fun setOpenDay(view: TextView, today: Today?) {

    var result = ""

    today?.let {

        val start = today.startTime.split(":")
        val end = today.endTime.split(":")

        result = "${setTime(start[0].toInt(), start[1].toInt())} ~ ${setTime(
            end[0].toInt(),
            end[1].toInt()
        )}"
    }

    view.text = result
}

@BindingAdapter("dayOpen")
fun setOpenDay(view: TextView, day: Day?) {

    var result = ""

    day?.let {

        val start = day.startTime.split(":")
        val end = day.endTime.split(":")

        result =
            "${setHour(start[0].toInt())}:${start[1].toInt()} ~ ${setHour(end[0].toInt())}:${end[1].toInt()}"
    }

    view.text = result
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
        hour.toString()
}

fun setMinute(min: Int): String {
    return if (min >= 10)
        min.toString() + ""
    else
        "0$min"
}

fun setTime(hour: Int, minute: Int): String {
    return """${setAmPm(hour)} ${setHour(hour)}:${setMinute(minute)}"""
}
