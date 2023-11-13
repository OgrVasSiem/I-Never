package com.foresko.gamenever.ui

import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction

@NavGraph(default = true)
annotation class RootNavGraph(val start: Boolean = false)

class RootNavigator(navigator: DestinationsNavigator) : DestinationsNavigator by navigator

fun RootNavigator.launchSingleTopNavigate(
    destination: Direction,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(destination) {
        launchSingleTop = true
        builder()
    }
}