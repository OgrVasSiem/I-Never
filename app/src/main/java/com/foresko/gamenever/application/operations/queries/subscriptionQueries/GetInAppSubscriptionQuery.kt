package com.foresko.gamenever.application.operations.queries.subscriptionQueries

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.foresko.gamenever.application.core.query.RemoteQuery
import com.foresko.gamenever.application.core.query.RemoteQueryHandler
import com.foresko.gamenever.application.core.readModels.InAppSubscription
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.toFlowCatching
import com.foresko.gamenever.core.utils.mapRight
import com.foresko.gamenever.graphql.InAppSubscriptionQuery
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

data class GetInAppSubscriptionQuery(val id: String) : RemoteQuery<InAppSubscription?>

class GetInAppSubscriptionQueryHandler @Inject constructor(
    private val apolloClient: ApolloClient
) : RemoteQueryHandler<GetInAppSubscriptionQuery, InAppSubscription?> {

    override fun handle(query: GetInAppSubscriptionQuery): Flow<Either<TechnicalError, InAppSubscription?>> {
        return apolloClient.query(InAppSubscriptionQuery(query.id))
            .toFlowCatching()
            .mapRight { apolloOperationResult ->
                when (apolloOperationResult) {
                    is ApolloOperationResult.Failure -> {
                        FirebaseCrashlytics.getInstance()
                            .recordException(ApolloOperationFailed(apolloOperationResult))

                        null
                    }

                    is ApolloOperationResult.Success ->
                        apolloOperationResult.data.node?.onInAppSubscription?.let { inAppSubscription ->
                            InAppSubscription(
                                id = inAppSubscription.id,
                                price = inAppSubscription.price.value,
                                currencyCode = inAppSubscription.price.currencyCode,
                                subscriptionName = inAppSubscription.sku
                            )
                        }
                }
            }.flowOn(Dispatchers.IO)
    }
}