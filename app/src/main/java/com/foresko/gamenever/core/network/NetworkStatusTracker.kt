package com.foresko.gamenever.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class NetworkStatusTracker(
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkStatus = callbackFlow {
        this.trySend(NetworkStatus.Unavailable).isSuccess

        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                try {
                    trySend(NetworkStatus.Available)
                } catch (e: Exception) {
                    launch {
                        delay(250)
                        trySend(NetworkStatus.Available)
                    }
                }
            }

            override fun onUnavailable() {
                try {
                    trySend(NetworkStatus.Unavailable)
                } catch (e: Exception) {
                    launch {
                        delay(250)
                        trySend(NetworkStatus.Unavailable)
                    }
                }
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)

        connectivityManager.activeNetwork

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback)
        }
    }
}