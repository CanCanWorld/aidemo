package com.zrq.aidemo.type

import androidx.annotation.Keep

@Keep
data class ChatItemType(
    val name: String,
    var content: String,
    val time: Long,
    val image: String,
    val isAi: Boolean
)