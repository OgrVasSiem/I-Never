package com.game.INever.ui.destination.main.components

import androidx.compose.runtime.MutableState
import com.game.INever.core.rest.Card

data class CardState(
    val cardData: MutableState<Card?>,
    val isSelected: MutableState<Boolean>,
    val showDialog: MutableState<Boolean>,

)
