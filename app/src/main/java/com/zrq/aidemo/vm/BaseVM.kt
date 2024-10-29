package com.zrq.aidemo.vm

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.tencent.mmkv.MMKV
import com.zrq.aidemo.common.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseVM(app: Application) : AndroidViewModel(app) {

    val mmkv: MMKV by lazy { MMKV.defaultMMKV() }

    val chatDao by lazy { AppDatabase.getInstance(app).chatDao() }

    suspend fun toast(msg: String) = withContext(Dispatchers.Main) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}