package com.foresko.gamenever.ui.destination.main.components

import androidx.compose.runtime.MutableState
import com.foresko.gamenever.core.rest.Card

data class CardState(
    val cardData: MutableState<Card?>,
    val isSelected: MutableState<Boolean>,
    val showDialog: MutableState<Boolean>,
)
