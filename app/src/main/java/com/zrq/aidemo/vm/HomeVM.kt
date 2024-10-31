package com.zrq.aidemo.vm

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.zrq.aidemo.common.db.ChatEntity
import com.zrq.aidemo.type.AIInfoType
import com.zrq.aidemo.type.ChatItemType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeVM(app: Application) : BaseVM(app) {

    val TAG = "HomeVM"

    private val systemAi = mutableListOf(
        AIInfoType("小糖", "你现在的人设是猫娘小糖，是我的女友😙", "https://img1.baidu.com/it/u=4065645778,1630449028&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"),
        AIInfoType(
            "蔡徐坤",
            "你现在的人设是练习时长两年半的个人练习生蔡徐坤，喜欢唱、跳、rap和篮球，是我的女友😙",
            "https://img2.baidu.com/it/u=3180711090,4079282331&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
        ),
        AIInfoType("刻晴", "你现在的人设是原神中的角色刻晴，是我的女友😙", "https://img2.baidu.com/it/u=3614783927,1754235341&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=802"),
        AIInfoType("可莉", "你现在的人设是原神中的角色可莉，是我的女友😙", "https://img2.baidu.com/it/u=3653038764,3357385993&fm=253&fmt=auto&app=138&f=JPEG?w=809&h=800"),
        AIInfoType("七七", "你现在的人设是原神中的角色七七，是我的女友😙", "https://img2.baidu.com/it/u=3584722155,3260676770&fm=253&fmt=auto&app=138&f=JPEG?w=712&h=712"),
        AIInfoType("珊瑚宫心海", "你现在的人设是原神中的角色珊瑚宫心海，是我的女友😙", "https://img1.baidu.com/it/u=3800072466,1146016346&fm=253&fmt=auto?w=800&h=1059"),
        AIInfoType("妮露", "你现在的人设是原神中的角色妮露，是我的女友😙", "https://img2.baidu.com/it/u=739688156,3790796266&fm=253&fmt=auto?w=891&h=800"),
        AIInfoType("神里绫华", "你现在的人设是原神中的角色神里绫华，是我的女友😙", "https://img1.baidu.com/it/u=2237600921,3639676453&fm=253&fmt=auto?w=800&h=800"),
        AIInfoType("甘雨", "你现在的人设是原神中的角色甘雨，是我的女友😙", "https://img2.baidu.com/it/u=3908856500,1541645081&fm=253&fmt=auto&app=138&f=JPEG?w=519&h=500"),
        AIInfoType("亚丝娜", "你现在的人设是《刀剑神域》中的角色亚丝娜，是我的女友😙", "https://img1.baidu.com/it/u=994527070,3798997047&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800"),
        AIInfoType("时崎狂三", "你现在的人设是《约会大作战》中的角色时崎狂三，是我的女友😙", "https://img2.baidu.com/it/u=2305122185,527435017&fm=253&fmt=auto&app=138&f=JPEG?w=300&h=300"),
        AIInfoType("蕾姆", "你现在的人设是《从零开始的异世界生活》中的角色蕾姆，是我的女友😙", "https://img0.baidu.com/it/u=1374187448,588652622&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"),
        AIInfoType("樱岛麻衣", "你现在的人设是《青春猪头少年系列》中的角色樱岛麻衣，是我的女友😙", "https://img1.baidu.com/it/u=927571529,343814929&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400"),
        AIInfoType("蝴蝶忍", "你现在的人设是《鬼灭之刃》中的角色蝴蝶忍，是我的女友😙", "https://img2.baidu.com/it/u=2533176229,2321839867&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=505"),
    )

    var keyword by mutableStateOf("")
    var isFocused by mutableStateOf(false)

    val aiList = mutableStateListOf<ChatItemType>()

    fun init() {
        viewModelScope.launch {
            launch {
                initAiLast()
            }
            launch {
                delay(100)
                initConfig()
            }
        }
    }

    private suspend fun initAiLast() {
        val aiFlow = chatDao.getAis()
        aiFlow.collect { ais ->
            Log.d(TAG, "ai: $ais")
            aiList.clear()
            ais.forEach { ai ->
                val lastChat = chatDao.getLastChat(ai)
                Log.d(TAG, "lastChat: $lastChat")
                if (lastChat.isConfig == 0) {
                    aiList.add(ChatItemType(ai, lastChat.content, lastChat.time, lastChat.image, true))
                } else {
                    val image = systemAi.first { it.name == ai }.image
                    Log.d(TAG, "image: $image")
                    aiList.add(ChatItemType(ai, "你好~", lastChat.time, image, true))
                }
            }
        }
    }

    private suspend fun initConfig() = withContext(Dispatchers.IO) {
        systemAi.forEach { ai ->
            launch {
                chatDao.getConfigByAiName(ai.name).collect {
                    if (it.isEmpty()) {
                        Log.d(TAG, "添加人设: ${ai.name} => ${ai.config}")
                        chatDao.insertChat(
                            ChatEntity(
                                ai.name,
                                ai.config,
                                System.currentTimeMillis(),
                                0,
                                1,
                                ai.image,
                                0
                            )
                        )
                    } else {
                        Log.d(TAG, "存在人设: ${ai.name} => ${it.toMutableList()}")
                    }
                }
            }
        }
    }

}