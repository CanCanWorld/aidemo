package com.zrq.aidemo.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tencent.mmkv.MMKV
import com.zrq.aidemo.LocalNavController
import com.zrq.aidemo.R
import com.zrq.aidemo.navigation.Route
import com.zrq.aidemo.screen.component.MainScreen
import com.zrq.aidemo.ui.theme.AIChatBg
import com.zrq.aidemo.ui.theme.White70
import com.zrq.aidemo.vm.SettingVM

@Composable
fun SettingScreen() {

    val navHostController = LocalNavController.current
    var isDialogShow by remember {
        mutableStateOf(false)
    }
    val vm : SettingVM = viewModel()

    LaunchedEffect(Unit) {
        vm.init()
    }

    MainScreen(
        painter = painterResource(id = R.drawable.img_user),
        title = "AI女友",
    ) {
        LazyColumn {
            item {
                RowItem(
                    "日志",
                    Icons.Filled.KeyboardArrowRight,
                    onClick = { navHostController.navigate(Route.LogRoute.route) }
                )
                RowItem(
                    "AI打字速度",
                    Icons.Filled.KeyboardArrowRight,
                    onClick = { isDialogShow = true }
                )
            }
        }
        if (isDialogShow) {
            Dialog(onDismissRequest = { isDialogShow = false }) {
                Column(
                    modifier = Modifier
                        .background(AIChatBg, shape = RoundedCornerShape(16.dp))
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "调节AI打字速度"
                    )
                    Slider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        value = vm.speed,
                        onValueChange = { newValue ->
                            vm.updateSpeed(newValue)
                        },
                        valueRange = 0f..3f,
                        steps = 2, // 0, 1, 2
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center, text = "慢")
                        Text(modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center, text = "中")
                        Text(modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center, text = "快")
                        Text(modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center, text = "极快")
                    }
                }
            }
        }
    }
}

@Composable
fun RowItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .shadow(5.dp, shape = RoundedCornerShape(10.dp))
            .background(AIChatBg)
            .clickable {
                onClick()
            }
            .padding(10.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = title)
        Icon(imageVector = icon, tint = White70, contentDescription = "")
    }
}