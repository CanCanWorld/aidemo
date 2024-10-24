package com.zrq.aidemo.vm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class HomeVM(app: Application) : AndroidViewModel(app){
    var keyword by mutableStateOf("")
    var isFocused by mutableStateOf(false)

}