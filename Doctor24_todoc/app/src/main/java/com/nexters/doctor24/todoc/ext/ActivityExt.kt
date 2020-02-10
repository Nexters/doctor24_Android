package com.nexters.doctor24.todoc.ext

import android.app.Activity

fun Activity?.isActivityDestroyed(): Boolean {
    return if (this == null) {
        true
    } else {
        var isDestroy = false
        isDestroy = this.isDestroyed || this.isFinishing
        isDestroy
    }
}