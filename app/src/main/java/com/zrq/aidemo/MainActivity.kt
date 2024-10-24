package com.zrq.aidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zrq.aidemo.navigation.MyNavHost
import com.zrq.aidemo.ui.theme.AidemoTheme

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AidemoTheme {
                CompositionLocalProvider(LocalNavController provides navController) {
                    MyNavHost()
                }
            }
        }
    }
}
