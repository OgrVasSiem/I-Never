package com.game.INever.ui.destination.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class GameScreenNavArgs(
    val ids: List<Long>
) : Parcelable