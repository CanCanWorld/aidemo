package com.zrq.aidemo.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zrq.aidemo.R
import com.zrq.aidemo.screen.home.component.ChatItem
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.ui.theme.MainBg
import com.zrq.aidemo.ui.theme.SearchBg
import com.zrq.aidemo.ui.theme.TextFieldBg
import com.zrq.aidemo.ui.theme.White
import com.zrq.aidemo.vm.HomeVM

@Composable
fun HomeScreen() {
    val vm: HomeVM = viewModel()
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
                Row(
                    modifier = Modifier.padding(top = 30.dp, bottom = 20.dp, start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = R.drawable.img_user),
                        contentDescription = "头像"
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "猫娘小糖", fontSize = 22.sp, fontWeight = FontWeight.ExtraLight)
                }
                Row {
                    Row(
                        modifier = Modifier
                            .padding(horizontal =  20.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TextFieldBg)
                            .onFocusChanged { vm.isFocused = it.isFocused }
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .padding(10.dp),
                        ) {
                            BasicTextField(
                                modifier = Modifier.fillMaxSize(),
                                value = vm.keyword,
                                onValueChange = {
                                    vm.keyword = it
                                }
                            ) { innerTextField ->
                                if (vm.keyword.isEmpty() && !vm.isFocused) {
                                    Text(
                                        modifier = Modifier.offset(y = (-2).dp),
                                        text = "Search..."
                                    )
                                }
                                innerTextField()
                            }
                        }

                        Icon(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SearchBg)
                                .size(40.dp)
                                .padding(10.dp),
                            painter = painterResource(id = R.drawable.img_search),
                            tint = White,
                            contentDescription = "搜索"
                        )
                    }
                }
                LazyColumn {
                    items(5) {
                        ChatItem(item = ChatItemType("猫娘小糖", "你好", "12:00"))
                    }
                }
            }
        }
    }
}


@Composable
fun Modifier.autoCloseKeyboard(): Modifier = composed {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isKeyboardOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    if (!isKeyboardOpen) {
        DisposableEffect(Unit) {
            focusManager.clearFocus()
            onDispose {  }
        }
    }
    pointerInput(this) {
        detectTapGestures(
            onPress = {
                keyboardController?.hide()
            }
        )
    }
}

