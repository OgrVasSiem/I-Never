package com.foresko.game.ui.theme

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
    val button: TextStyle,
    val title: TextStyle,
    val cards: TextStyle,
    val nameCards: TextStyle,
    val boldText: TextStyle,
    val rulesText: TextStyle,
    val boldButtonText: TextStyle,
    val subButtonText: TextStyle,
    val titleCards: TextStyle,
    val textCards: TextStyle
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
            fontWeight = FontWeight(400),
            lineHeight = 20.sp,
            letterSpacing = 0.2f.sp
        ),
        title = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(600),
            lineHeight = 22.sp,
            letterSpacing = (-0.41f).sp
        ),
        cards = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight(300),
            lineHeight = 16.sp,
            letterSpacing = (-0.24f).sp
        ),
        nameCards = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(400),
            lineHeight = 22.sp,
            letterSpacing = (-0.24f).sp
        ),
        boldText = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight(600),
            lineHeight = 22.sp,
            letterSpacing = (-0.45f).sp
        ),
        rulesText = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            lineHeight = 22.sp,
            letterSpacing = (-0.45f).sp
        ),
        boldButtonText = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight(600),
            lineHeight = 24.sp,
            letterSpacing = (-0.24f).sp
        ),
        subButtonText = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            lineHeight = 17.sp,
            letterSpacing = (-0.24f).sp
        ),
        titleCards = TextStyle(
            fontSize = 36.sp,
            fontWeight = FontWeight(700),
            lineHeight = 22.sp,
            letterSpacing = (-0.45f).sp
        ),
        textCards = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight(500),
            lineHeight = 50.sp,
            letterSpacing = (-0.45f).sp
        ),
    )
}

val LocalINeverStyles = staticCompositionLocalOf { INeverStyles() }