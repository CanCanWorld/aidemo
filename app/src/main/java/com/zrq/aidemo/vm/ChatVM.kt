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
    val configList = mutableStateListOf<ChatItemType>()
    val chatDao by lazy { AppDatabase.getInstance(app).chatDao() }

    fun sendMessage(msg: String) {
        Log.d(TAG, "sendMessage: $msg")
        if (msg.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            val map = mutableMapOf<String, Any>()
            map["model"] = "glm-4-plus"

            val messages = mutableListOf<Delta>()
            configList.forEach { config ->
                Log.d(TAG, "输入人设: ${config.content}")
                messages.add(Delta(config.content, "system"))
            }
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
                            chatDao.insertChat(ChatEntity(aiName, aiMessage, System.currentTimeMillis(), isAi = 1, isConfig = 0))
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
        viewModelScope.launch {
            chatDao.insertChat(ChatEntity(aiName, message, System.currentTimeMillis(), isAi = 0, isConfig = 0))
        }
        message = ""
    }

    fun getChatList(name: String) {
        aiName = name
        viewModelScope.launch {
            val chats = chatDao.getChatsByAiName(aiName)
            chats.collect { chat ->
                chatList.clear()
                chat.forEach {
                    Log.d(TAG, "chatList: $it")
                    chatList.add(ChatItemType(it.name, it.content, it.time, it.isAi == 1))
                }
                if (chatList.isEmpty()) {
                    Log.d(TAG, "没有聊天记录，自动发送消息")
                    if (configList.isEmpty()) {
                        Log.d(TAG, "没有人设，等待人设获取")
                        delay(500)
                    }
                    sendMessage("你好")
                }
            }
        }
    }

    fun getConfigList(name: String) {
        aiName = name
        viewModelScope.launch {
            val chats = chatDao.getConfigByAiName(aiName)
            chats.collect { chat ->
                chat.forEach {
                    Log.d(TAG, "configList: $it")
                    configList.add(ChatItemType(it.name, it.content, it.time, it.isAi == 1))
                }
            }
        }
    }
}