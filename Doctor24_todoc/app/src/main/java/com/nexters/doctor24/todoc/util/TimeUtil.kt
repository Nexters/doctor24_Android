package com.nexters.doctor24.todoc.util

import java.text.SimpleDateFormat
import java.util.*

val DATE_FORMAT = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
val HOUR_FORMAT = SimpleDateFormat("hh", Locale.KOREA)

fun String.isFasterThan(endTime: String) : Boolean {
    val startDate = DATE_FORMAT.parse(this)?.time
    val endDate = DATE_FORMAT.parse(endTime)?.time
    startDate?.let{
        endDate?.let{
            return startDate < endDate
        }
    } ?: return true
}

fun String.isLaterThan(startTime: String) : Boolean {
    val endDate = DATE_FORMAT.parse(this)?.time
    val startDate = DATE_FORMAT.parse(startTime)?.time
    startDate?.let{
        endDate?.let{
            return endDate > startDate
        }
    } ?: return true
}

fun String.toHour() : Int {
    return HOUR_FORMAT.parse(this)!!.hours
}

fun isCurrentMapDarkMode() = when (getCurrentTimeHours()) {
    in 8..18 -> false
    else -> true
}

private fun getCurrentTimeHours(): Int {
    val now = System.currentTimeMillis()
    val date = Date(now)

    return SimpleDateFormat("HH", Locale.KOREA).format(date).toInt()
}