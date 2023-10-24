package com.game.INever.ui.destination.rulesScreen

import androidx.compose.runtime.Composable
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet

@Composable
@Destination(style = DestinationStyleBottomSheet::class)
@RootNavGraph
fun RulesScreen(
    rootNavigator: RootNavigator
) {
}