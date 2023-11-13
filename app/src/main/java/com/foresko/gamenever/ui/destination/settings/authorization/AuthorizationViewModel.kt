package com.foresko.gamenever.ui.destination.settings.authorization

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignOutCommand
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetPremiumQuery
import com.foresko.gamenever.core.google.AuthResult
import com.foresko.gamenever.core.network.NetworkStatus
import com.foresko.gamenever.core.network.NetworkStatusTracker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val queryDispatcher: QueryDispatcher,
    private val networkStatusTracker: NetworkStatusTracker,
    private val commandDispatcher: CommandDispatcher,
    val authResult: AuthResult
): ViewModel(){
    val account = GoogleSignIn.getLastSignedInAccount(context)

    var authorizationState by mutableStateOf(false)
        private set

    var premiumIsActive by mutableStateOf(false)
        private set

    var isNetworkConnectionError by mutableStateOf(false)
        private set

    fun changeNetworkConnectionErrorState(isNetworkConnectionError: Boolean) {
        this.isNetworkConnectionError = isNetworkConnectionError
    }

    fun signOutGoogleAccount() {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                if (it is NetworkStatus.Available) {
                    GoogleSignIn.getClient(
                        context, GoogleSignInOptions.Builder().build()
                    ).signOut().apply {
                        commandDispatcher.dispatch(SignOutCommand)
                    }

                    authorizationState = true
                } else {
                    delay(500)

                    changeNetworkConnectionErrorState(true)
                }
            }
        }
    }

    fun authorization(task: Task<GoogleSignInAccount>?) {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                if (it is NetworkStatus.Available) {
                    authResult.processTask(task)

                    authorizationState = true
                } else {
                    delay(500)
                    changeNetworkConnectionErrorState(true)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            queryDispatcher.dispatch(GetPremiumQuery).collectLatest {
                premiumIsActive = it.isActive
            }
        }
    }
}