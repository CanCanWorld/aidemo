package com.zrq.aidemo.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.zrq.aidemo.screen.home.autoCloseKeyboard
import com.zrq.aidemo.ui.theme.MainBg

@Composable
fun MainScreen(
    painter: Painter,
    title: String,
    onImageClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .autoCloseKeyboard()
                .background(MainBg)
                .padding(paddingValues)
                .imePadding()
        ) {
            Row(
                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp, start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(10.dp, shape = CircleShape)
                        .clickable { onImageClick() },
                    painter = painter,
                    contentDescription = "头像"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = title, fontSize = 22.sp, fontWeight = FontWeight.ExtraLight)
            }
            content()
        }
    }
}