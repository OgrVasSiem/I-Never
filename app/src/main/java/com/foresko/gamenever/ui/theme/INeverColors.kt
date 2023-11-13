package com.foresko.gamenever.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class INeverColors(
    white: Color,
    bg: Color,
    card: Color,
    primary: Color,
    secondary: Color,
    placeholder: Color,
    divider: Color,
    accent: Color,
    grey: Color,
    overlay: Color,
    destructive: Color,
    button: Color,
    textColor: Color
) {

    var white: Color by mutableStateOf(white)
        private set

    var bg: Color by mutableStateOf(bg)
        private set

    var card: Color by mutableStateOf(card)
        private set

    var primary: Color by mutableStateOf(primary)
        private set

    var secondary: Color by mutableStateOf(secondary)
        private set

    var placeholder: Color by mutableStateOf(placeholder)
        private set

    var divider: Color by mutableStateOf(divider)
        private set

    var accent: Color by mutableStateOf(accent)
        private set

    var grey: Color by mutableStateOf(grey)
        private set

    var overlay: Color by mutableStateOf(overlay)
        private set

    var destructive: Color by mutableStateOf(destructive)
        private set

    var button: Color by mutableStateOf(button)
        private set

    var textColor: Color by mutableStateOf(textColor)
        private set


    fun copy(): INeverColors {
        return INeverColors(
            white = white,
            bg = bg,
            card = card,
            primary = primary,
            secondary = secondary,
            placeholder = placeholder,
            divider = divider,
            accent = accent,
            grey = grey,
            overlay = overlay,
            destructive = destructive,
            button = button,
            textColor = textColor
        )
    }

    fun updateColorsFrom(other: INeverColors) {
        white = other.white
        bg = other.bg
        card = other.card
        primary = other.primary
        secondary = other.secondary
        placeholder = other.placeholder
        divider = other.divider
        accent = other.accent
        grey = other.grey
        overlay = other.overlay
        destructive = other.destructive
        button = other.button
        textColor = other.textColor
    }

    override fun toString(): String {
        return """INeverColors(
            white=$white,
            bg=$bg,
            card=$card,
            primary=$primary,
            secondary=$secondary,
            placeholder=$placeholder,
            divider=$divider,
            accent=$accent,
            grey=$grey,
            overlay=$overlay,
            destructive=$destructive,
            button=$button,
            textColor=$textColor
        )"""
    }
}

fun lightColors(): INeverColors = INeverColors(
    white = Color(0xFFFFFFFF),
    bg = Color(0xFF060709),
    card = Color(0xFF1E2037),
    primary = Color(0xFF000000),
    secondary = Color(0x852C3D56),
    placeholder = Color(0x47112B52),
    divider = Color(0xFF343D4E),
    accent = Color(0xFFED7878),
    grey = Color(0xFF938AAF),
    overlay = Color(0x80000000),
    destructive = Color(0xFFCE4949),
    button = Color(0xFFFF857A),
    textColor = Color(0xFF101826)
)

val LocalINeverColors = staticCompositionLocalOf { lightColors() }