package com.foresko.gamenever.application.operations.queries.subscriptionQueries

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.foresko.gamenever.application.core.query.RemoteQuery
import com.foresko.gamenever.application.core.query.RemoteQueryHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.toFlowCatching
import com.foresko.gamenever.core.utils.mapRight
import com.foresko.gamenever.graphql.InAppSubscriptionPurchaseQuery
import com.foresko.gamenever.ui.destination.premium.pendingStatusPurchaseScreen.PurchaseStatus
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

data class GetInAppSubscriptionPurchaseQuery(val id: String) :
    RemoteQuery<PurchaseStatus?>

class GetInAppSubscriptionPurchaseQueryHandler @Inject constructor(
    private val apolloClient: ApolloClient
) : RemoteQueryHandler<GetInAppSubscriptionPurchaseQuery, PurchaseStatus?> {

    override fun handle(query: GetInAppSubscriptionPurchaseQuery): Flow<Either<TechnicalError, PurchaseStatus?>> {
        return apolloClient.query(InAppSubscriptionPurchaseQuery(query.id))
            .toFlowCatching()
            .mapRight { apolloOperationResult ->
                when (apolloOperationResult) {
                    is ApolloOperationResult.Failure -> {
                        FirebaseCrashlytics.getInstance()
                            .recordException(ApolloOperationFailed(apolloOperationResult))

                        null
                    }

                    is ApolloOperationResult.Success ->
                        apolloOperationResult.data.node?.onInAppSubscriptionPurchase?.let { purchase ->
                            when (purchase.status.name) {
                                "CANCELED" -> PurchaseStatus.Canceled
                                "CONFIRMED" -> PurchaseStatus.Confirmed
                                "PENDING" -> PurchaseStatus.Pending
                                else -> PurchaseStatus.Canceled
                            }
                        }
                }
            }.flowOn(Dispatchers.IO)
    }
}
