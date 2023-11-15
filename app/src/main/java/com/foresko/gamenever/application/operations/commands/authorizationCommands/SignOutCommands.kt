package com.foresko.gamenever.application.operations.commands.authorizationCommands

import android.content.Context
import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.foresko.gamenever.application.core.command.RemoteCommand
import com.foresko.gamenever.application.core.command.RemoteCommandHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.executeCatching
import com.foresko.gamenever.core.utils.emptyString
import com.foresko.gamenever.dataStore.SessionDataStore
import com.foresko.gamenever.graphql.SignOutMutation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
sealed class SignOutCommandResult {

    object Success : SignOutCommandResult()

    data class UnknownError(val message: String) : SignOutCommandResult()
}

object SignOutCommand : RemoteCommand<SignOutCommandResult>

class SignOutCommandHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apolloClient: ApolloClient,
    private val sessionDataStore: SessionDataStore
) :
    RemoteCommandHandler<SignOutCommand, SignOutCommandResult> {

    private val mutex = Mutex()

    override suspend fun handle(command: SignOutCommand): Either<TechnicalError, SignOutCommandResult> {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                GoogleSignIn.getClient(
                    context, GoogleSignInOptions.Builder().build()
                ).signOut().run {
                    signOut()
                }
            }
        }
    }

    private suspend fun signOut(): Either<TechnicalError, SignOutCommandResult> {
        return apolloClient
            .mutation(SignOutMutation())
            .executeCatching()
            .map { apolloOperationResult ->
                when (apolloOperationResult) {
                    is ApolloOperationResult.Failure -> {
                        FirebaseCrashlytics.getInstance()
                            .recordException(ApolloOperationFailed(apolloOperationResult))

                        getUnknownError(
                            message = ApolloOperationFailed(apolloOperationResult).message
                                ?: emptyString
                        )
                    }

                    is ApolloOperationResult.Success -> {
                        val data = apolloOperationResult.data.signOut!!

                        if (data.onSignOutSuccessResult != null) {
                            sessionDataStore.updateData { null }

                            SignOutCommandResult.Success
                        } else {
                            getUnknownError(message = data.onError?.message ?: "")
                        }
                    }
                }
            }
    }

    private fun getUnknownError(message: String): SignOutCommandResult.UnknownError {
        return SignOutCommandResult.UnknownError(
            message = message
        )
    }
}