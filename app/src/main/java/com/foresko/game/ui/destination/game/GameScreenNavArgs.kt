package com.foresko.game.ui.destination.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class GameScreenNavArgs(
    val ids: LongArray,
    val fromAd: Boolean = false
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameScreenNavArgs

        if (!ids.contentEquals(other.ids)) return false

        return true
    }

    override fun hashCode(): Int {
        return ids.contentHashCode()
    }
}
