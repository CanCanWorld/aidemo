package com.zrq.aidemo.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zrq.aidemo.LocalNavController
import com.zrq.aidemo.R
import com.zrq.aidemo.screen.home.autoCloseKeyboard
import com.zrq.aidemo.type.ChatItemType
import com.zrq.aidemo.ui.theme.White70

@Composable
fun ChatItem(item: ChatItemType) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val navHostController = LocalNavController.current
    Row(
        modifier = Modifier
            .autoCloseKeyboard()
            .fillMaxSize()
            .height(90.dp)
            .padding(10.dp)
            .clickable {
                keyboardController?.hide()
                navHostController.navigate("chat/${item.name}")
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .shadow(10.dp, shape = CircleShape),
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
        Box(modifier = Modifier.fillMaxHeight()) {
            Text(text = item.time.toString(), fontSize = 12.sp)
        }
    }
}