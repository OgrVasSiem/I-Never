package com.game.INever.core.network

sealed class NetworkStatus {

    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()
}
