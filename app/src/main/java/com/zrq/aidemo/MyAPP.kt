package com.zrq.aidemo

import android.app.Application
import com.tencent.mmkv.MMKV

class MyAPP : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }

}