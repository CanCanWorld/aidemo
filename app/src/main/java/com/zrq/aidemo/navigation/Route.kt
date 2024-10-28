package com.zrq.aidemo.navigation

sealed class Route(val route : String) {
    data object HomeRoute : Route("home")
    data object ChatRoute : Route("chat")
    data object SettingRoute : Route("setting")
    data object LogRoute : Route("log")
}