package com.zrq.aidemo.screen.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.zrq.aidemo.R
import com.zrq.aidemo.screen.chat.component.AiChatItem
import com.zrq.aidemo.screen.chat.component.UserChatItem
import com.zrq.aidemo.screen.component.MainScreen
import com.zrq.aidemo.screen.home.autoCloseKeyboard
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.ui.theme.MainBg
import com.zrq.aidemo.ui.theme.SendBg
import com.zrq.aidemo.ui.theme.TextFieldBg
import com.zrq.aidemo.ui.theme.White
import com.zrq.aidemo.vm.ChatVM
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(name: String) {

    val vm: ChatVM = viewModel()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        Log.d("TAG", "ChatScreen: 页面重组")
        launch {
            vm.getConfigList(name)
        }.join()
        launch {
            vm.getChatList(name) {
                if (vm.chatList.size - 1 > 0) {
                    listState.scrollToItem(vm.chatList.size - 1)
                }
            }
        }
    }

    MainScreen(
        painter = rememberImagePainter(data = vm.aiImage),
        title = name
    ) {
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = listState,
            ) {
                items(vm.chatList) {
                    if (it.isAi) {
                        AiChatItem(item = it)
                    } else {
                        UserChatItem(item = it)
                    }
                }
                if (vm.aiMessage != "") {
                    item {
                        AiChatItem(
                            item = ChatItemType(vm.aiName, vm.aiMessage, System.currentTimeMillis(), vm.aiImage, true),
                            vm.isLoading
                        )
                    }
                }
            }
            // 输入框
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TextFieldBg)
                        .onFocusChanged { vm.isFocused = it.isFocused },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .padding(10.dp),
                    ) {
                        BasicTextField(
                            modifier = Modifier.fillMaxSize(),
                            textStyle = TextStyle(color = White),
                            enabled = !vm.isLoading,
                            value = vm.message,
                            onValueChange = {
                                vm.message = it
                            }
                        ) { innerTextField ->
                            if (vm.message.isEmpty() && !vm.isFocused) {
                                Text(
                                    modifier = Modifier.offset(y = (-2).dp),
                                    text = "Write..."
                                )
                            }
                            innerTextField()
                        }
                    }

                    if (vm.isLoading) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {

                        Icon(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SendBg)
                                .size(40.dp)
                                .clickable {
                                    val msg = vm.message
                                    vm.addUserMessage()
                                    vm.sendMessage(msg)
                                }
                                .padding(10.dp),
                            painter = painterResource(id = R.drawable.img_message),
                            tint = White,
                            contentDescription = "消息"
                        )
                    }
                }
            }
        }
    }
}