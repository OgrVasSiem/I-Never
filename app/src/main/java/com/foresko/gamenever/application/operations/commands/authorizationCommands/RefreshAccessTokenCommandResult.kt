package com.foresko.gamenever.application.operations.commands.authorizationCommands

import arrow.core.Either
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.foresko.gamenever.application.core.command.RemoteCommand
import com.foresko.gamenever.application.core.command.RemoteCommandHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.executeCatching
import com.foresko.gamenever.dataStore.SessionDataStore
import com.foresko.gamenever.graphql.RefreshAccessTokenMutation
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Qualifier

sealed class RefreshAccessTokenCommandResult {

    data class Success(val accessToken: String) : RefreshAccessTokenCommandResult()

    object SessionNotFound : RefreshAccessTokenCommandResult()

    data class UnknownError(val message: String) : RefreshAccessTokenCommandResult()
}

data class RefreshAccessTokenCommand(
    val accessToken: String
) : RemoteCommand<RefreshAccessTokenCommandResult>

@Qualifier
annotation class UnauthenticatedApolloClient

class RefreshAccessTokenCommandHandlerImpl @Inject constructor(
    @UnauthenticatedApolloClient private val apolloClient: ApolloClient,
    private val sessionDataStore: SessionDataStore
) :
    RemoteCommandHandler<RefreshAccessTokenCommand, RefreshAccessTokenCommandResult> {

    private val mutex = Mutex()

    override suspend fun handle(command: RefreshAccessTokenCommand): Either<TechnicalError, RefreshAccessTokenCommandResult> {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                val session = sessionDataStore.data.firstOrNull()
                    ?: return@withContext RefreshAccessTokenCommandResult.SessionNotFound.right()

                if (command.accessToken != session.accessToken) {
                    return@withContext RefreshAccessTokenCommandResult.Success(accessToken = session.accessToken)
                        .right()
                }

                apolloClient
                    .mutation(RefreshAccessTokenMutation(refreshToken = session.refreshToken))
                    .executeCatching()
                    .map { apolloOperationResult ->
                        when (apolloOperationResult) {
                            is ApolloOperationResult.Failure -> {
                                FirebaseCrashlytics.getInstance()
                                    .recordException(ApolloOperationFailed(apolloOperationResult))

                                RefreshAccessTokenCommandResult.UnknownError(
                                    message = ApolloOperationFailed(apolloOperationResult).message
                                        ?: ""
                                )
                            }

                            is ApolloOperationResult.Success -> {
                                val data = apolloOperationResult.data.refreshAccessToken!!

                                if (data.onRefreshAccessTokenSuccessResult != null) {
                                    sessionDataStore.updateData {
                                        session.copy(
                                            accessToken = data.onRefreshAccessTokenSuccessResult.accessToken.toString(),
                                            refreshToken = data.onRefreshAccessTokenSuccessResult.refreshToken.toString(),
                                        )
                                    }

                                    RefreshAccessTokenCommandResult.Success(
                                        accessToken = data.onRefreshAccessTokenSuccessResult.accessToken.toString()
                                    )
                                } else {
                                    RefreshAccessTokenCommandResult.UnknownError(
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