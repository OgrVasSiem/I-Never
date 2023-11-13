package com.foresko.gamenever.core.google

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignInWithGoogleCommand
import com.foresko.gamenever.core.network.NetworkStatus
import com.foresko.gamenever.core.network.NetworkStatusTracker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthorizationManager(
    private val networkStatusTracker: NetworkStatusTracker,
    private val commandDispatcher: CommandDispatcher,
    private val context: Context,
    ioDispatcher: CoroutineDispatcher
) {
    val coroutineScope = CoroutineScope(ioDispatcher + SupervisorJob())

    fun init() {
        coroutineScope.launch {
            networkStatusTracker.networkStatus.collectLatest { networkStatus ->
                if (networkStatus is NetworkStatus.Available) {
                    val account by mutableStateOf(GoogleSignIn.getLastSignedInAccount(context))

                    if (account != null) {
                        commandDispatcher.dispatch(SignInWithGoogleCommand(account?.idToken ?: ""))
                    }
                }
            }
        }
    }
}