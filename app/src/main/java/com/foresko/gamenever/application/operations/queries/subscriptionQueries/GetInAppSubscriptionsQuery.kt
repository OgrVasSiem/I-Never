package com.foresko.gamenever.application.operations.queries.subscriptionQueries

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.foresko.gamenever.application.core.query.RemoteQuery
import com.foresko.gamenever.application.core.query.RemoteQueryHandler
import com.foresko.gamenever.application.core.readModels.InAppSubscription
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.toFlowCatching
import com.foresko.gamenever.core.utils.mapRight
import com.foresko.gamenever.graphql.InAppSubscriptionsQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

object GetInAppSubscriptionsQuery : RemoteQuery<List<InAppSubscription>>

class GetInAppSubscriptionsQueryHandler @Inject constructor(
    private val apolloClient: ApolloClient
) : RemoteQueryHandler<GetInAppSubscriptionsQuery, List<InAppSubscription>> {

    override fun handle(query: GetInAppSubscriptionsQuery): Flow<Either<TechnicalError, List<InAppSubscription>>> {
        return apolloClient.query(InAppSubscriptionsQuery())
            .toFlowCatching()
            .mapRight { apolloOperationResult ->
                when (apolloOperationResult) {
                    is ApolloOperationResult.Failure -> {
                        FirebaseCrashlytics.getInstance()
                            .recordException(ApolloOperationFailed(apolloOperationResult))

                        emptyList()
                    }

                    is ApolloOperationResult.Success -> {
                        var inAppSubscriptions = emptyList<InAppSubscription>()

                        apolloOperationResult.data.inAppSubscriptions?.forEach { inAppSubscription ->
                            val subscription = InAppSubscription(
                                id = inAppSubscription.id,
                                price = inAppSubscription.price.value,
                                currencyCode = inAppSubscription.price.currencyCode,
                                subscriptionName = inAppSubscription.sku
                            )

                            inAppSubscriptions = inAppSubscriptions + subscription
                        }

                        inAppSubscriptions
                    }
                }
            }.flowOn(Dispatchers.IO)
    }
}
