package com.zrq.aidemo.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zrq.aidemo.R
import com.zrq.aidemo.screen.home.component.ChatItem
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.ui.theme.MainBg

@Composable
fun HomeScreen() {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBg)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 30.dp, horizontal = 20.dp),
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

