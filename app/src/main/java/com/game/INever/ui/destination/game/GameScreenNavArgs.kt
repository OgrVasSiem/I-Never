package com.game.INever.ui.destination.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class GameScreenNavArgs(
    val ids: String
) : Parcelable {
    fun getIdsList(): List<Long> {
        return ids.split(",").map { it.toLong() }
    }
}
