package com.foresko.game.ui.destination.main.components

import androidx.compose.runtime.MutableState
import com.foresko.game.core.rest.Card

data class CardState(
    val cardData: MutableState<Card?>,
    val isSelected: MutableState<Boolean>,
    val showDialog: MutableState<Boolean>,

)
