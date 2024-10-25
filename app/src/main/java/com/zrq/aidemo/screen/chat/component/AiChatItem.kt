package com.zrq.aidemo.screen.chat.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import com.zrq.aidemo.ui.theme.White70

@Composable
fun AiChatItem(
    item: ChatItemType,
    loading: Boolean = false,
) {
    Box(modifier = Modifier.padding(10.dp)) {
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AIChatBg)
                .padding(vertical = 10.dp, horizontal = 20.dp),
            text = item.content,
            fontSize = 14.sp
        )
        Image(
            modifier = Modifier.size(26.dp),
            painter = painterResource(id = R.drawable.img_user),
            contentDescription = item.name
        )
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 5.dp)
                    .size(10.dp)
                    .align(Alignment.BottomEnd),
                strokeWidth = 2.dp,
                color = White70
            )
        }
    }

}