package com.zrq.aidemo.screen.chat.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zrq.aidemo.R
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.ui.theme.AIChatBg
import com.zrq.aidemo.ui.theme.UserChatBg

@Composable
fun UserChatItem(item: ChatItemType) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(UserChatBg)
                .padding(vertical = 10.dp, horizontal = 20.dp),
            text = item.content,
            fontSize = 14.sp
        )
    }

}