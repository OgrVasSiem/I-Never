package com.foresko.gamenever.ui.destination.premium.authorization

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.core.readModels.InAppSubscription
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignInWithGoogleCommand
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetPremiumQuery
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionQuery
import com.foresko.gamenever.core.google.AuthResult
import com.foresko.gamenever.core.network.NetworkStatus
import com.foresko.gamenever.core.network.NetworkStatusTracker
import com.foresko.gamenever.ui.destinations.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationSBPViewModel @Inject constructor(
    private val queryDispatcher: QueryDispatcher,
    private val commandDispatcher: CommandDispatcher,
    val authResult: AuthResult,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val subscriptionId = savedStateHandle.navArgs<AuthorizationSBPNavArgs>().subscriptionId

    var authorizationSBPState by mutableStateOf(AuthorizationSBPState.NONE)
        private set

    var inAppSubscription by mutableStateOf<InAppSubscription?>(null)
        private set

    var isNetworkConnectionError by mutableStateOf(false)
        private set

    fun changeNetworkConnectionErrorState(isNetworkConnectionError: Boolean) {
        this.isNetworkConnectionError = isNetworkConnectionError
    }

    fun authorization(task: Task<GoogleSignInAccount>?) {
        viewModelScope.launch {
            when (commandDispatcher.dispatch(SignInWithGoogleCommand(task = task))) {
                is Either.Right -> {}

                is Either.Left -> changeNetworkConnectionErrorState(true)
            }
        }
    }

    init {
        viewModelScope.launch {
            queryDispatcher.dispatch(GetInAppSubscriptionQuery(id = subscriptionId))
                .collectLatest { subscription ->
                    when (subscription) {
                        is Either.Left -> {
                            Log.e(
                                "AuthorizationSBPViewModel",
                                "technicalError = ${subscription.value}"
                            )
                        }

                        is Either.Right -> {
                            inAppSubscription = subscription.value
                        }
                    }
                }
        }

        viewModelScope.launch {
            combine(
                queryDispatcher.dispatch(GetPremiumQuery),
                queryDispatcher.dispatch(GetSessionQuery)
            ) { premium, session ->
                premium to session
            }.collectLatest {
                when {
                    it.second != null && it.first.isActive -> authorizationSBPState =
                        AuthorizationSBPState.WITH_PREMIUM

                    it.second != null && !it.first.isActive -> authorizationSBPState =
                        AuthorizationSBPState.WITHOUT_PREMIUM
                }
            }
        }
    }
}