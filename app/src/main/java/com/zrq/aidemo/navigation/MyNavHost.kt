package com.zrq.aidemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zrq.aidemo.LocalNavController
import com.zrq.aidemo.screen.chat.ChatScreen
import com.zrq.aidemo.screen.home.HomeScreen

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.HomeRoute.route
    ) {
        composable(Route.HomeRoute.route) {
            HomeScreen()
        }
        composable(Route.ChatRoute.route) {
            ChatScreen()
        }
    }
}