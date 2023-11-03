package com.foresko.game.ui.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

@Stable
fun Brush.Companion.horizontalGradient(
    colorStops: List<Pair<Float, Color>>,
    startX: Float = 0.0f,
    endX: Float = Float.POSITIVE_INFINITY,
    tileMode: TileMode = TileMode.Clamp
) = horizontalGradient(
    colorStops = colorStops.toTypedArray(),
    startX = startX,
    endX = endX,
    tileMode = tileMode
)

@Stable
fun Brush.Companion.linearGradient(
    colorStops: List<Pair<Float, Color>>,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite,
    tileMode: TileMode = TileMode.Clamp
) = linearGradient(
    colorStops = colorStops.toTypedArray(),
    start = start,
    end = end,
    tileMode = tileMode
)