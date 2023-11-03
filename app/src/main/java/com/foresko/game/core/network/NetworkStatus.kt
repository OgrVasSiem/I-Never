package com.foresko.game.core.network

sealed class NetworkStatus {

    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()
}
