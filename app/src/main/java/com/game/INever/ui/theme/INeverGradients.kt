package com.game.INever.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class INeverGradients(
    gradient: List<Pair<Float, Color>>,
    gradient2: List<Pair<Float, Color>>,
    gradientDark2: List<Pair<Float, Color>>,
    stroke: List<Pair<Float, Color>>,
    accent2: List<Pair<Float, Color>>,
    accent2WithAlpha: List<Pair<Float, Color>>,
    gradientDark: List<Pair<Float, Color>>,
) {

    var gradient: List<Pair<Float, Color>> by mutableStateOf(gradient)
        private set

    var gradient2: List<Pair<Float, Color>> by mutableStateOf(gradient2)
        private set

    var gradientDark2: List<Pair<Float, Color>> by mutableStateOf(gradientDark2)
        private set

    var stroke: List<Pair<Float, Color>> by mutableStateOf(stroke)
        private set

    var accent2: List<Pair<Float, Color>> by mutableStateOf(accent2)
        private set

    var accent2WithAlpha: List<Pair<Float, Color>> by mutableStateOf(accent2WithAlpha)
        private set

    var gradientDark: List<Pair<Float, Color>> by mutableStateOf(gradientDark)
        private set

    fun copy(): INeverGradients = INeverGradients(
        gradient = gradient,
        gradient2 = gradient2,
        gradientDark2 = gradientDark2,
        stroke = stroke,
        accent2 = accent2,
        accent2WithAlpha = accent2WithAlpha,
        gradientDark = gradientDark
    )

    fun updateGradientsFrom(other: INeverGradients) {
        gradient = other.gradient
        gradient2 = other.gradient2
        gradientDark2 = other.gradientDark2
        stroke = other.stroke
        accent2 = other.accent2
        gradientDark = other.gradientDark
    }

    override fun toString(): String {
        return "INeverGradients(gradient=$gradient, gradient2=$gradient2, gradientDark2=$gradientDark2, stroke=$stroke, accent2=$accent2, gradientDark=$gradientDark, accent2WithAlpha=$accent2WithAlpha)"
    }
}

fun lightGradients() = INeverGradients(
    gradient = listOf(
        0F to Color(0xFFFFD4B9).copy(alpha = 0.00f),
        1F to Color(0xFFFFD4B9).copy(alpha = 0.26f),
    ),
    gradient2 = listOf(
        0F to Color(0xFFFEB9FF).copy(alpha = 0.00f),
        1F to Color(0xFFFEB9FF).copy(alpha = 0.26f),
    ),
    stroke = listOf(
        0F to Color(0xFFFFDBC1).copy(alpha = 0.81f),
        0.5F to Color(0xFFFFDBC1).copy(alpha = 0.20f),
        1F to Color(0xFFFFDBC1).copy(alpha = 0.60f),
    ),
    accent2 = listOf(
        0F to Color(0xFFFDC2C2),
        1F to Color(0xFFC584BB),
    ),
    accent2WithAlpha = listOf(
        0F to Color(0xFFFDC2C2).copy(alpha = 0.30f),
        1F to Color(0xFFC584BB).copy(alpha = 0.30f),
    ),
    gradientDark = listOf(
        0F to Color(0xFF111229),
        0.5F to Color(0xFF252851),
        1F to Color(0xFF42438A),
    ),
    gradientDark2 = listOf(
        0F to Color(0xFF1A1C34),
        1F to Color(0xFF060709),
    )
)

val LocalINeverGradients = staticCompositionLocalOf {
    lightGradients()
}