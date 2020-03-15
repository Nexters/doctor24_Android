package com.nexters.doctor24.todoc.util

import android.graphics.Typeface
import android.widget.TextView

fun TextView.selectStyle(select: Boolean) {
    isSelected = select
    setTypeface(null, if(select) Typeface.BOLD else Typeface.NORMAL)
}