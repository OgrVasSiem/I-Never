package com.game.INever.ui.destination.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
@RootNavGraph
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    Box(modifier = Modifier.background(Color.Green)){
        Text(text = "rekommkob")
    }
}