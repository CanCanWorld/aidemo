package com.zrq.aidemo.type

import androidx.annotation.Keep

@Keep
data class Choice(
    val delta: Delta,
    val finish_reason: String,
    val index: Int
)