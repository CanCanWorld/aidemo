package com.zrq.aidemo.type

data class ChatItemType(
    val name: String,
    var content: String,
    val time: Long,
    val isAi: Boolean
)