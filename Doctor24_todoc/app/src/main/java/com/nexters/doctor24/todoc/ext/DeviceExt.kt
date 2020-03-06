package com.nexters.doctor24.todoc.ext

import android.content.res.Resources

fun Float.dpToPixel(): Float = this * Resources.getSystem().displayMetrics.density