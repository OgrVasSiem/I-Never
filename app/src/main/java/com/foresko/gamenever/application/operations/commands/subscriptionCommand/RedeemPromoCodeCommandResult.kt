package com.foresko.gamenever.application.operations.commands.subscriptionCommand

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.foresko.gamenever.application.core.command.RemoteCommand
import com.foresko.gamenever.application.core.command.RemoteCommandHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.executeCatching
import com.foresko.gamenever.graphql.RedeemPromoCodeMutation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class RedeemPromoCodeCommandResult {

    data class Success(val sku: String) : RedeemPromoCodeCommandResult()

    data class UnknownError(val message: String) : RedeemPromoCodeCommandResult()
}

data class RedeemPromoCodeCommand(
    val promoCode: String
) : RemoteCommand<RedeemPromoCodeCommandResult>

class RedeemPromoCodeCommandHandlerImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : RemoteCommandHandler<RedeemPromoCodeCommand, RedeemPromoCodeCommandResult> {

    private val mutex = Mutex()

    override suspend fun handle(command: RedeemPromoCodeCommand): Either<TechnicalError, RedeemPromoCodeCommandResult> {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                apolloClient
                    .mutation(RedeemPromoCodeMutation(promoCode = command.promoCode))
                    .executeCatching()
                    .map { apolloOperationResult ->
                        when (apolloOperationResult) {
                            is ApolloOperationResult.Failure -> {
                                FirebaseCrashlytics.getInstance()
                                    .recordException(ApolloOperationFailed(apolloOperationResult))

                                RedeemPromoCodeCommandResult.UnknownError(
                                    message = ApolloOperationFailed(apolloOperationResult).message
                                        ?: ""
                                )
                            }

                            is ApolloOperationResult.Success -> {
                                val data = apolloOperationResult.data.redeemPromoCode!!

                                if (data.onRedeemPromoCodeSuccessResult != null) {
                                    RedeemPromoCodeCommandResult.Success(
                                        data.onRedeemPromoCodeSuccessResult.activatedSubscription.sku
                                    )
                                } else {
                                    RedeemPromoCodeCommandResult.UnknownError(
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
