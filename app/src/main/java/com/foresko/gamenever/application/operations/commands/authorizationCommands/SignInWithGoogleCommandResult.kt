package com.foresko.gamenever.application.operations.commands.authorizationCommands

import android.content.Context
import android.util.Log
import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.foresko.gamenever.application.core.command.RemoteCommand
import com.foresko.gamenever.application.core.command.RemoteCommandHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.executeCatching
import com.foresko.gamenever.core.utils.emptyString
import com.foresko.gamenever.dataStore.Session
import com.foresko.gamenever.dataStore.SessionDataStore
import com.foresko.gamenever.graphql.SignInWithGoogleMutation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class SignInWithGoogleCommandResult {

    object Success : SignInWithGoogleCommandResult()

    data class UnknownError(val message: String) : SignInWithGoogleCommandResult()
}

data class SignInWithGoogleCommand(
    val task: Task<GoogleSignInAccount>?
) : RemoteCommand<SignInWithGoogleCommandResult>

class SignInWithGoogleCommandHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apolloClient: ApolloClient,
    private val sessionDataStore: SessionDataStore
) : RemoteCommandHandler<SignInWithGoogleCommand, SignInWithGoogleCommandResult> {

    private val mutex = Mutex()

    override suspend fun handle(command: SignInWithGoogleCommand): Either<TechnicalError, SignInWithGoogleCommandResult> {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val account = GoogleSignIn.getLastSignedInAccount(context)

                    if (account != null) {
                        signInWithGoogle(account = account)
                    } else {
                        command.task?.getResult(ApiException::class.java)?.let { accountInit ->
                            signInWithGoogle(account = accountInit)
                        } ?: Either.Right(getUnknownError(message = "task is null"))
                    }
                } catch (e: ApiException) {
                    FirebaseCrashlytics.getInstance().recordException(e)

                    Either.Right(
                        getUnknownError(message = e.message ?: emptyString)
                    )
                }
            }
        }
    }

    private suspend fun signInWithGoogle(account: GoogleSignInAccount): Either<TechnicalError, SignInWithGoogleCommandResult> {
        return apolloClient
            .mutation(SignInWithGoogleMutation(idToken = account.idToken.orEmpty()))
            .executeCatching()
            .map { apolloOperationResult ->
                when (apolloOperationResult) {
                    is ApolloOperationResult.Failure -> {
                        FirebaseCrashlytics.getInstance()
                            .recordException(ApolloOperationFailed(apolloOperationResult))

                        signOutFromGoogle(context = context)

                        getUnknownError(
                            ApolloOperationFailed(apolloOperationResult).message ?: emptyString
                        )
                    }

                    is ApolloOperationResult.Success -> {
                        val data = apolloOperationResult.data.signInWithGoogle!!

                        if (data.onSignInWithGoogleSuccessResult != null) {
                            sessionDataStore.updateData {
                                Session(
                                    accessToken = data.onSignInWithGoogleSuccessResult.accessToken.toString(),
                                    refreshToken = data.onSignInWithGoogleSuccessResult.refreshToken.toString(),
                                    email = account.email ?: emptyString
                                )
                            }

                            SignInWithGoogleCommandResult.Success
                        } else {
                            signOutFromGoogle(context = context)

                            getUnknownError(data.onError?.message ?: emptyString)
                        }
                    }
                }
            }
    }

    private fun signOutFromGoogle(context: Context) {
        GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder().build()
        ).signOut()
    }

    private fun getUnknownError(message: String): SignInWithGoogleCommandResult.UnknownError {
        return SignInWithGoogleCommandResult.UnknownError(
            message = message
        )
    }
}