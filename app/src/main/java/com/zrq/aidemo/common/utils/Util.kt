package com.zrq.aidemo.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

fun Long.timerFormat(): String {
    val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
    return sdf.format(this)
}