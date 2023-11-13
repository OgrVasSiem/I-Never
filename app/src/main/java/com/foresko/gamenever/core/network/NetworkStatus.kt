package com.foresko.gamenever.core.network

sealed class NetworkStatus {

    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()
}
