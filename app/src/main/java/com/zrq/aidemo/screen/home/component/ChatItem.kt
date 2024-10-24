package com.zrq.aidemo.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zrq.aidemo.R
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.ui.theme.White70

@Composable
fun ChatItem(item: ChatItemType) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = R.drawable.img_user),
            contentDescription = "头像"
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = item.name, fontSize = 14.sp, fontWeight = FontWeight.Normal)
            Text(
                text = item.content,
                fontSize = 10.sp,
                color = White70,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(text = item.time, fontSize = 12.sp)
    }
}