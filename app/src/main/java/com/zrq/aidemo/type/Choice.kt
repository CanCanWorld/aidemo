package com.zrq.aidemo.type

data class Choice(
    val delta: Delta,
    val finish_reason: String,
    val index: Int
)