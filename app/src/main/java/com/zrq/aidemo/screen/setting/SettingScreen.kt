package com.zrq.aidemo.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zrq.aidemo.common.utils.timerFormat
import com.zrq.aidemo.screen.home.autoCloseKeyboard
import com.zrq.aidemo.ui.theme.MainBg
import com.zrq.aidemo.ui.theme.White
import com.zrq.aidemo.ui.theme.White70
import com.zrq.aidemo.vm.SettingVM

@Composable
fun SettingScreen() {

    val vm: SettingVM = viewModel()

    LaunchedEffect(Unit) {
        vm.init()
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .autoCloseKeyboard()
                .background(MainBg)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn {
                    item {
                        Row {
                            TableText(modifier = Modifier.weight(1f), text = "name")
                            TableText(modifier = Modifier.weight(1f), text = "content")
                            TableText(modifier = Modifier.weight(1f), text = "time")
                            TableText(modifier = Modifier.weight(1f), text = "isAi")
                            TableText(modifier = Modifier.weight(1f), text = "isConfig")
                        }
                    }
                    items(vm.list) {
                        Row {
                            TableText(modifier = Modifier.weight(1f), text = it.name)
                            TableText(modifier = Modifier.weight(1f), text = it.content)
                            TableText(modifier = Modifier.weight(1f), text = it.time.timerFormat())
                            TableText(modifier = Modifier.weight(1f), text = (it.isAi == 1).toString())
                            TableText(modifier = Modifier.weight(1f), text = (it.isConfig == 1).toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(2.dp).border(1.dp, White70),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 10.sp,
        maxLines = 2,
        lineHeight = 14.sp,
        overflow = TextOverflow.Ellipsis
    )
}