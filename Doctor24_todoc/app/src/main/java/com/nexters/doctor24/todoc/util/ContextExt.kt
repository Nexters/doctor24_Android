package com.nexters.doctor24.todoc.util

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager

fun Context.dpToPx(dp: Int) : Int =
    (dp * resources.displayMetrics.density).toInt()

fun getScreenWidth(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

val Int.toPx: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toPxFloat: Float
    get() = (this / Resources.getSystem().displayMetrics.density)

val Int.toDpFloat: Float
    get() = (this * Resources.getSystem().displayMetrics.density)