package com.foresko.game.ui

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@NavGraph(default = true)
annotation class RootNavGraph(val start: Boolean = false)

class RootNavigator(navigator: DestinationsNavigator) : DestinationsNavigator by navigator