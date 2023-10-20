package com.game.INever.ui.destination.game

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
@RootNavGraph
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
}