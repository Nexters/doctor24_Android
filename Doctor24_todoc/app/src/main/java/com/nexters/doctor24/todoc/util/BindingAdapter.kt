package com.nexters.doctor24.todoc.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nexters.doctor24.todoc.data.detailed.Today

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = View.VISIBLE
    } else
        view.visibility = View.GONE
}

@BindingAdapter("android:today_open")
fun setOpenDay(view: TextView, today : Today) {
    view.text = "${today.startTime} ~ ${today.endTime}"
}

@BindingAdapter("android:category_text")
fun setCategoryText(view: TextView, categories : List<String>) {
    var category_text = ""
    for (i in categories){
        category_text += "$i ,"
    }
    view.text = category_text
}