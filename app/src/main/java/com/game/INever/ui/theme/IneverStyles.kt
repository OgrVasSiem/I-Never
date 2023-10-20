package com.game.INever.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class INeverStyles(
    val body: TextStyle,
    val body2: TextStyle,
    val bodySemiBold: TextStyle,
    val header: TextStyle,
    val name1: TextStyle,
    val name2: TextStyle,
    val text1: TextStyle,
    val text2: TextStyle,
    val button: TextStyle
) {

    constructor() : this(
        body = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            lineHeight = 20.sp,
            letterSpacing = 0.4f.sp
        ),
        body2 = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
            lineHeight = 22.sp
        ),
        bodySemiBold = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight(600),
            lineHeight = 20.sp,
            letterSpacing = 0.4f.sp
        ),
        header = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            lineHeight = 20.sp
        ),
        name1 = TextStyle(
            fontSize = 42.sp,
            fontWeight = FontWeight(200),
            lineHeight = 42.sp,
            letterSpacing = 1.6f.sp
        ),
        name2 = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight(100),
            lineHeight = 20.sp,
            letterSpacing = (-0.24f).sp
        ),
        text1 = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
            lineHeight = 21.sp,
            letterSpacing = 0.4f.sp
        ),
        text2 = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            lineHeight = 23.sp,
            letterSpacing = 0.4f.sp
        ),
        button = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight(300),
            lineHeight = 20.sp,
            letterSpacing = 0.2f.sp
        )
    )
}

val LocalINeverStyles = staticCompositionLocalOf { INeverStyles() }