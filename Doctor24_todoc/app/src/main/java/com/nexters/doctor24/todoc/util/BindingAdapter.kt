package com.nexters.doctor24.todoc.util

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.nexters.doctor24.todoc.data.detailed.response.Today

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = View.VISIBLE
    } else
        view.visibility = View.GONE
}

@BindingAdapter("todayOpen")
fun setOpenDay(view: TextView, today : Today?) {

     var result =""

    today?.let {
        result = "${today.startTime} ~ ${today.endTime}"
    }
    view.text = result
}

@BindingAdapter("categoryText")
fun setCategoryText(view: TextView, categories : List<String>?) {
    var category_text = ""

    categories?.let {
        for (i in categories){
            category_text += "$i ,"
        }
    }
    view.text = category_text
}

@BindingAdapter("android:background")
fun setBackground(view: View, @DrawableRes resId: Int?) {
    resId?.let {
        view.setBackgroundResource(it)
    }
}
