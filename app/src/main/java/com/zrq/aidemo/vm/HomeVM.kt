package com.zrq.aidemo.vm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.zrq.aidemo.type.ChatItemType

class HomeVM(app: Application) : AndroidViewModel(app) {
    var keyword by mutableStateOf("")
    var isFocused by mutableStateOf(false)

    val aiList = listOf(
        ChatItemType("蔡徐坤", "hello!", System.currentTimeMillis(), true),
        ChatItemType("刻晴", "你好！", System.currentTimeMillis(), true),
    )

}