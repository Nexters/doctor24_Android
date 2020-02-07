package com.nexters.doctor24.todoc.util

import android.view.View
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible : Boolean){
    if (isVisible){
        view.visibility = View.VISIBLE
    }else
        view.visibility = View.GONE
}

@BindingAdapter("android:background")
fun setBackground(view: View, @DrawableRes resId: Int?) {
    resId?.let {
        view.setBackgroundResource(it)
    }
}
