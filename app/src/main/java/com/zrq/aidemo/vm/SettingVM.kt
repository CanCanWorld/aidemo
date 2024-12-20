package com.zrq.aidemo.vm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.tencent.mmkv.MMKV

class SettingVM(app: Application) : BaseVM(app) {

    val TAG = "LogVM"
    var speed by mutableFloatStateOf(0f)

    fun updateSpeed(newValue: Float) {
        speed = newValue
        mmkv.putInt("speed", newValue.toInt())
    }

    fun init() {
        speed = mmkv.getInt("speed", 1).toFloat()
    }


}