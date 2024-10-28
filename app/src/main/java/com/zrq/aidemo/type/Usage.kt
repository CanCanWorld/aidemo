package com.zrq.aidemo.type

import androidx.annotation.Keep

@Keep
data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)