package com.zrq.aidemo.vm

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zrq.aidemo.common.db.AppDatabase
import com.zrq.aidemo.common.db.ChatEntity
import kotlinx.coroutines.launch

class SettingVM(app: Application): AndroidViewModel(app) {

    val TAG = "SettingVM"
    val chatDao by lazy { AppDatabase.getInstance(app).chatDao() }
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