package com.zrq.aidemo.vm

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.zrq.aidemo.common.db.ChatEntity
import com.zrq.aidemo.common.network.RetrofitClient.okHttpClient
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.type.DataType
import com.zrq.aidemo.type.Delta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.math.abs

class ChatVM(app: Application) : BaseVM(app) {

    val TAG = "ChatVM"
    var aiName by mutableStateOf("")
    var aiImage by mutableStateOf("")
    var message by mutableStateOf("")
    var aiMessage by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    var isFocused by mutableStateOf(false)
    val chatList = mutableStateListOf<ChatItemType>()
    val configList = mutableStateListOf<ChatItemType>()
    var onchange: suspend () -> Unit = {}

    fun sendMessage(msg: String) {
        Log.d(TAG, "sendMessage: $msg")
        if (msg.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val map = mutableMapOf<String, Any>()
            map["model"] = "glm-4-flash"
            val messages = mutableListOf<Delta>()
            configList.forEach { config ->
                Log.d(TAG, "输入人设: $config")
                aiImage = config.image
                messages.add(Delta(config.content, "system"))
            }
            messages.add(Delta(msg, "user"))
            map["messages"] = messages
            map["stream"] = true
            val json = Gson().toJson(map)
            Log.d(TAG, "json: $json")
            aiMessage = "..."
            isLoading = true
            requestChat(json.toRequestBody("application/json".toMediaType()))
                .catch {
                    isLoading = false
                    aiMessage = ""
                    toast("请求失败")
                    it.printStackTrace()
                }
                .collect {
                    if (it == "[DONE]") {
                        isLoading = false
                        chatDao.insertChat(ChatEntity(aiName, aiMessage, System.currentTimeMillis(), isAi = 1, isConfig = 0, aiImage))
                        aiMessage = ""
                    } else {
                        isLoading = true
                        aiMessage += it
                        launch(Dispatchers.Main) {
                            onchange()
                        }
                    }
                }
        }
    }

    private fun requestChat(body: RequestBody) = flow {
        val speed = mmkv.getInt("speed", 2)
        val delay = abs(3 - speed) * 100
        Log.d(TAG, "delay: $delay")
        val request = Request.Builder()
            .url("https://open.bigmodel.cn/api/paas/v4/chat/completions")
            .post(body)
            .build()
        val response = okHttpClient.newCall(request).execute()
        response.body!!.let {
            aiMessage = ""
            val reader = it.byteStream().bufferedReader()
            with(reader) {
                while (true) {
                    val line = readLine()
                    if (line.isNullOrEmpty()) continue
                    val data = line.substringAfter("data: ")
                    if (data == "[DONE]") {
                        emit("[DONE]")
                        break
                    }
                    val chat = Gson().fromJson(data, DataType::class.java)
                    val content = chat.choices[0].delta.content
                    Log.d("zrq", "content: $content")
                    delay(delay.toLong())
                    emit(content)
                }
                close()
            }
        }
    }

    fun addUserMessage() {
        if (message.isBlank()) return
        viewModelScope.launch {
            chatDao.insertChat(ChatEntity(aiName, message, System.currentTimeMillis(), isAi = 0, isConfig = 0, aiImage))
        }
        message = ""
    }


    fun getChatList(name: String, onChange: suspend () -> Unit) {
        onchange = onChange
        aiName = name
        Log.d(TAG, "getChatList: $aiName")
        viewModelScope.launch {
            val chats = chatDao.getChatsByAiName(aiName)
            chats.collect { chat ->
                chatList.clear()
                chat.forEach {
                    Log.d(TAG, "chatList: $it")
                    aiImage = it.image
                    chatList.add(ChatItemType(it.name, it.content, it.time, it.image, it.isAi == 1))
                }
                if (chatList.isEmpty()) {
                    Log.d(TAG, "没有聊天记录，自动发送消息")
                    if (configList.isEmpty()) {
                        Log.d(TAG, "没有人设，等待人设获取")
                        delay(500)
                    }
                    sendMessage("你好")
                } else {
                    onChange()
                }
            }
        }
    }

    fun getConfigList(name: String) {
        Log.d(TAG, "getConfigList: $aiName")
        aiName = name
        viewModelScope.launch {
            val chats = chatDao.getConfigByAiName(aiName)
            chats.collect { chat ->
                chat.forEach {
                    Log.d(TAG, "configList: $it")
                    configList.add(ChatItemType(it.name, it.content, it.time, it.image, it.isAi == 1))
                }
            }
        }
    }
}