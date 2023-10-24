package com.game.INever.core.rest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cards(
    val topics: List<Cards>,
    val name: String,
    val color: String,
    val image: String,
    val freeTopic: Boolean,
    val alertImage: String,
    val questions: List<String>,
) : Parcelable


