package com.zrq.aidemo.vm

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.zrq.aidemo.common.db.AppDatabase
import com.zrq.aidemo.common.db.ChatEntity
import com.zrq.aidemo.common.network.RetrofitClient.okHttpClient
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.type.DataType
import com.zrq.aidemo.type.Delta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ChatVM(app: Application) : AndroidViewModel(app) {

    val TAG = "ChatVM"
    var aiName by mutableStateOf("")
    var message by mutableStateOf("")
    var aiMessage by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    var isFocused by mutableStateOf(false)
    val chatList = mutableStateListOf<ChatItemType>()
    val chatDao by lazy { AppDatabase.getInstance(app).chatDao() }

    fun sendMessage(msg: String) {
        Log.d(TAG, "sendMessage: $msg")
        if (msg.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            val map = mutableMapOf<String, Any>()
            map["model"] = "glm-4-plus"
            val messages = mutableListOf<Delta>()
            messages.add(Delta("你现在的人设是练习时长两年半的个人练习生蔡徐坤，喜欢唱、跳、rap和篮球，😙", "user"))
            messages.add(Delta(msg, "user"))
            map["messages"] = messages
            map["stream"] = true
            val json = Gson().toJson(map)
            Log.d(TAG, "json: $json")
            val request = Request.Builder()
                .url("https://open.bigmodel.cn/api/paas/v4/chat/completions")
                .post(json.toRequestBody("application/json".toMediaType()))
                .build()
            val response = okHttpClient.newCall(request).execute()
            response.body?.let {
                val reader = it.byteStream().bufferedReader()
                with(reader) {
                    while (true) {
                        val line = readLine()
                        if (line.isNullOrEmpty()) continue
                        val data = line.substringAfter("data: ")
                        if (data == "[DONE]") {
                            isLoading = false
                            chatList.add(ChatItemType(aiName, aiMessage, System.currentTimeMillis(), true))
                            chatDao.insertChat(ChatEntity(aiName, aiMessage, System.currentTimeMillis(), isAi = true, isConfig = false))
                            aiMessage = ""
                            break
                        }
                        val chat = Gson().fromJson(data, DataType::class.java)
                        val content = chat.choices[0].delta.content
                        Log.d("zrq", "content: $content")
                        delay(100)
                        aiMessage += content
                    }
                    close()
                }
            }
        }
    }

    fun addUserMessage() {
        if (message.isBlank()) return
        chatList.add(ChatItemType("user", message, System.currentTimeMillis(), false))
        chatDao.insertChat(ChatEntity("user", message, System.currentTimeMillis(), isAi = false, isConfig = false))
        message = ""
    }

    fun getChatList(name: String) {
        aiName = name
        viewModelScope.launch {
            val chats = chatDao.getChatsByAiName(aiName)
            chats.collect { chat->
                Log.d(TAG, "getChatList: $chat")
                chat.forEach {
                    chatList.add(ChatItemType(it.name, it.content, it.time, it.isAi))
                }
                Log.d(TAG, "chatList: $chatList")
                Log.d(TAG, "isEmpty: ${chatList.isEmpty()}")
                if (chatList.isEmpty()) {
                    sendMessage("你好")
                }
            }
        }
    }
}