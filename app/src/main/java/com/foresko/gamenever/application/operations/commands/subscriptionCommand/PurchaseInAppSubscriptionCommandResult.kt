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
import com.foresko.gamenever.graphql.PurchaseInAppSubscriptionMutation
import com.foresko.gamenever.graphql.type.PlatformName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class PurchaseInAppSubscriptionCommandResult {

    data class Success(val confirmationUrl: String, val id: String) :
        PurchaseInAppSubscriptionCommandResult()

    data class UnknownError(val message: String) : PurchaseInAppSubscriptionCommandResult()
}

data class PurchaseInAppSubscriptionCommand(
    val subscriptionId: String
) : RemoteCommand<PurchaseInAppSubscriptionCommandResult>

class PurchaseInAppSubscriptionCommandHandlerImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : RemoteCommandHandler<PurchaseInAppSubscriptionCommand, PurchaseInAppSubscriptionCommandResult> {

    private val mutex = Mutex()

    override suspend fun handle(command: PurchaseInAppSubscriptionCommand): Either<TechnicalError, PurchaseInAppSubscriptionCommandResult> {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                apolloClient
                    .mutation(command.toMutation())
                    .executeCatching()
                    .map { apolloOperationResult ->
                        when (apolloOperationResult) {
                            is ApolloOperationResult.Failure -> {
                                FirebaseCrashlytics.getInstance()
                                    .recordException(ApolloOperationFailed(apolloOperationResult))

                                PurchaseInAppSubscriptionCommandResult.UnknownError(
                                    message = ApolloOperationFailed(apolloOperationResult).message
                                        ?: ""
                                )
                            }

                            is ApolloOperationResult.Success -> {
                                val data = apolloOperationResult.data.purchaseInAppSubscription!!

                                if (data.onPurchaseInAppSubscriptionSuccessResult != null) {
                                    PurchaseInAppSubscriptionCommandResult.Success(
                                        confirmationUrl = data.onPurchaseInAppSubscriptionSuccessResult
                                            .purchase.confirmationUrl ?: "",
                                        id = data.onPurchaseInAppSubscriptionSuccessResult.purchase.id
                                    )
                                } else {
                                    PurchaseInAppSubscriptionCommandResult.UnknownError(
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

private fun PurchaseInAppSubscriptionCommand.toMutation() =
    PurchaseInAppSubscriptionMutation(
        platformName = PlatformName.ANDROID,
        subscriptionId = subscriptionId
    )