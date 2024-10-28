package com.zrq.aidemo.type

import androidx.annotation.Keep

@Keep
data class Delta(
    val content: String,
    val role: String
)