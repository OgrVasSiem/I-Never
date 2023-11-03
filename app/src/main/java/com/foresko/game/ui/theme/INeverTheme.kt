package com.foresko.game.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object INeverTheme {

    val colors: INeverColors
        @Composable
        @ReadOnlyComposable
        get() = LocalINeverColors.current

    val gradients: INeverGradients
        @Composable
        @ReadOnlyComposable
        get() = LocalINeverGradients.current

    val textStyles: INeverStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalINeverStyles.current
}

@Composable
fun INeverTheme(content: @Composable () -> Unit) {
    val rippleIndication = rememberRipple()

    CompositionLocalProvider(
        LocalIndication provides rippleIndication,
    ) {
        val systemUiController = rememberSystemUiController()

        LaunchedEffect(systemUiController) {
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = false,
                transformColorForLightContent = { Color(0x66000000) }
            )

            systemUiController.isNavigationBarContrastEnforced = true

            if (systemUiController.isNavigationBarContrastEnforced) {
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = false,
                    transformColorForLightContent = { Color(0x66000000) }
                )
            } else {
                systemUiController.setNavigationBarColor(
                    color = Color(0xE6FFFFFF),
                    darkIcons = false,
                    transformColorForLightContent = { Color(0x66000000) }
                )
            }
        }

        content()
    }
}