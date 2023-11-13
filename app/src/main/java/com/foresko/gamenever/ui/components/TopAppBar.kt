package com.foresko.gamenever.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    startItem: @Composable (modifier: Modifier) -> Unit = {},
    middleItem: @Composable (modifier: Modifier) -> Unit = {},
    endItem: @Composable (modifier: Modifier) -> Unit = {}
) {
    androidx.compose.material.TopAppBar(
        contentPadding = PaddingValues(TopAppBarDefaults.ZeroDp),
        backgroundColor = Color.Transparent,
        elevation = TopAppBarDefaults.ZeroDp,
        modifier = Modifier.statusBarsPadding()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            startItem(modifier = Modifier.align(Alignment.CenterStart))

            middleItem(modifier = Modifier.align(Alignment.Center))

            endItem(modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

object TopAppBarDefaults {
    val DefaultButtonHorizontalPaddingValues = 8.dp

    val ZeroDp = 0.dp
}