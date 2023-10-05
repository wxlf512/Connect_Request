package dev.wxlf.connectrequest.request_ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.requestGraph() {
    navigation(startDestination = RequestRoutes.MAIN.route, route = RequestRoutes.REQUEST.route) {
        composable(RequestRoutes.MAIN.route) {
            RequestScreen()
        }
    }
}

enum class RequestRoutes(val route: String) {
    REQUEST("request"),
    MAIN("main")
}