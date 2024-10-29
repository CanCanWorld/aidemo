package com.zrq.aidemo.vm

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.zrq.aidemo.common.db.ChatEntity
import kotlinx.coroutines.launch

class LogVM(app: Application): BaseVM(app) {

    val TAG = "LogVM"
    val list = mutableStateListOf<ChatEntity>()

    fun init() {
        viewModelScope.launch {
            chatDao.getChats().collect {
                Log.d(TAG, "getChats: $it")
                list.addAll(it)
            }
        }
    }


}