package com.foresko.gamenever.ui.destination.settings.authorization

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignInWithGoogleCommand
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignOutCommand
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetPremiumQuery
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQuery
import com.foresko.gamenever.core.google.AuthResult
import com.foresko.gamenever.dataStore.Session
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val queryDispatcher: QueryDispatcher,
    private val commandDispatcher: CommandDispatcher,
    val authResult: AuthResult
): ViewModel(){
    var session by mutableStateOf<Session?>(null)
        private set

    var authorizationState by mutableStateOf(false)
        private set

    var premiumIsActive by mutableStateOf<Boolean?>(null)
        private set

    var isNetworkConnectionError by mutableStateOf(false)
        private set

    fun changeNetworkConnectionErrorState(isNetworkConnectionError: Boolean) {
        this.isNetworkConnectionError = isNetworkConnectionError
    }

    fun signOutGoogleAccount() {
        viewModelScope.launch {
            viewModelScope.launch {
                when (commandDispatcher.dispatch(SignOutCommand)) {
                    is Either.Right -> authorizationState = true

                    is Either.Left -> changeNetworkConnectionErrorState(true)
                }
            }
        }
    }

    fun authorization(task: Task<GoogleSignInAccount>?) {
        viewModelScope.launch {
            when (commandDispatcher.dispatch(SignInWithGoogleCommand(task = task))) {
                is Either.Right -> {
                    premiumIsActive = null

                    authorizationState = true
                }

                is Either.Left -> changeNetworkConnectionErrorState(true)
            }
        }
    }

    init {
        viewModelScope.launch {
            queryDispatcher.dispatch(GetPremiumQuery).collectLatest {


                premiumIsActive = it.isActive
            }
        }

        viewModelScope.launch {
            session = queryDispatcher.dispatch(GetSessionQuery).firstOrNull()
        }
    }
}