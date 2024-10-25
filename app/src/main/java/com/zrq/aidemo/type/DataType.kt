package com.zrq.aidemo.type

data class DataType(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val usage: Usage
)