package com.nexters.doctor24.todoc.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible : Boolean){
    if (isVisible){
        view.visibility = View.VISIBLE
    }else
        view.visibility = View.GONE
}
