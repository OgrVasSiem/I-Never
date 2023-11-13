package com.foresko.gamenever.application.operations.commands.authorizationCommands

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient

import com.foresko.gamenever.application.core.command.RemoteCommand
import com.foresko.gamenever.application.core.command.RemoteCommandHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.executeCatching
import com.foresko.gamenever.dataStore.SessionDataStore
import com.foresko.gamenever.graphql.SignOutMutation
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
    private val apolloClient: ApolloClient,
    private val sessionDataStore: SessionDataStore
) :
    RemoteCommandHandler<SignOutCommand, SignOutCommandResult> {

    private val mutex = Mutex()

    override suspend fun handle(command: SignOutCommand): Either<TechnicalError, SignOutCommandResult> {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                apolloClient
                    .mutation(SignOutMutation())
                    .executeCatching()
                    .map { apolloOperationResult ->
                        when (apolloOperationResult) {
                            is ApolloOperationResult.Failure -> {
                                FirebaseCrashlytics.getInstance()
                                    .recordException(ApolloOperationFailed(apolloOperationResult))

                                SignOutCommandResult.UnknownError(
                                    message = ApolloOperationFailed(apolloOperationResult).message
                                        ?: ""
                                )
                            }

                            is ApolloOperationResult.Success -> {
                                val data = apolloOperationResult.data.signOut!!

                                if (data.onSignOutSuccessResult != null) {
                                    sessionDataStore.updateData { null }

                                    SignOutCommandResult.Success
                                } else {
                                    SignOutCommandResult.UnknownError(
                                        message = data.onError?.message ?: ""
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }
}