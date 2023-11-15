package com.foresko.gamenever.application.operations.queries.subscriptionQueries

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.foresko.gamenever.application.core.query.RemoteQuery
import com.foresko.gamenever.application.core.query.RemoteQueryHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.toFlowCatching
import com.foresko.gamenever.core.utils.mapRight
import com.foresko.gamenever.dataStore.Premium
import com.foresko.gamenever.graphql.InfoAboutUserSubscriptionQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

object GetInfoAboutUserSubscriptionQuery : RemoteQuery<Premium?>

class GetInfoAboutUserSubscriptionQueryHandler @Inject constructor(
    private val apolloClient: ApolloClient
) : RemoteQueryHandler<GetInfoAboutUserSubscriptionQuery, Premium?> {

    override fun handle(query: GetInfoAboutUserSubscriptionQuery): Flow<Either<TechnicalError, Premium?>> {
        return apolloClient.query(InfoAboutUserSubscriptionQuery())
            .toFlowCatching()
            .mapRight { apolloOperationResult ->
                when (apolloOperationResult) {
                    is ApolloOperationResult.Failure -> {
                        FirebaseCrashlytics.getInstance()
                            .recordException(ApolloOperationFailed(apolloOperationResult))

                        null
                    }

                    is ApolloOperationResult.Success -> {
                        apolloOperationResult.data.viewer?.inAppSubscription?.let { info ->
                            Premium(
                                isActive = info.isActive,
                                expiryDateTime = info.expirationDateTime.toInstant().toEpochMilli(),
                            )
                        }
                    }
                }
            }.flowOn(Dispatchers.IO)
    }
}
