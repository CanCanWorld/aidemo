package com.zrq.aidemo.vm

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zrq.aidemo.common.db.AppDatabase
import com.zrq.aidemo.common.db.ChatEntity
import com.zrq.aidemo.type.ChatItemType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeVM(app: Application) : AndroidViewModel(app) {

    val TAG = "HomeVM"

    val chatDao by lazy { AppDatabase.getInstance(app).chatDao() }
    val systemAi = mutableMapOf<String, String>(
        "蔡徐坤" to "你现在的人设是练习时长两年半的个人练习生蔡徐坤，喜欢唱、跳、rap和篮球，😙",
        "刻晴" to "你现在的人设是原神中的角色刻晴，😙",
    )

    fun init() {
        viewModelScope.launch {
            systemAi.forEach { (name, content) ->
                launch {
                    chatDao.getConfigByAiName(name).collect {
                        if (it.isEmpty()) {
                            Log.d(TAG, "添加人设: $name => $content")
                            chatDao.insertChat(
                                ChatEntity(
                                    name,
                                    content,
                                    System.currentTimeMillis(),
                                    0,
                                    1,
                                    0
                                )
                            )
                        } else {
                            Log.d(TAG, "存在人设: $name => ${it.toMutableList()}")
                        }
                    }
                }
            }
        }
    }

    var keyword by mutableStateOf("")
    var isFocused by mutableStateOf(false)

    val aiList = listOf(
        ChatItemType("蔡徐坤", "hello!", System.currentTimeMillis(), true),
        ChatItemType("刻晴", "你好！", System.currentTimeMillis(), true),
    )

}