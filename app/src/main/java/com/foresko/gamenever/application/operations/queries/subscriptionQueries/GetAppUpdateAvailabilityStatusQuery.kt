package com.foresko.gamenever.application.operations.queries.subscriptionQueries

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.foresko.gamenever.BuildConfig
import com.foresko.gamenever.application.core.query.RemoteQuery
import com.foresko.gamenever.application.core.query.RemoteQueryHandler
import com.foresko.gamenever.core.apollo.ApolloOperationFailed
import com.foresko.gamenever.core.apollo.ApolloOperationResult
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.apollo.toFlowCatching
import com.foresko.gamenever.core.utils.mapRight
import com.foresko.gamenever.core.versionControl.VersionStatusType
import com.foresko.gamenever.graphql.AppUpdateAvailabilityStatusQuery
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

object GetAppUpdateAvailabilityStatusQuery : RemoteQuery<VersionStatusType?>

class GetAppUpdateAvailabilityStatusQueryHandler @Inject constructor(
    private val apolloClient: ApolloClient
) : RemoteQueryHandler<GetAppUpdateAvailabilityStatusQuery, VersionStatusType?> {

    override fun handle(query: GetAppUpdateAvailabilityStatusQuery): Flow<Either<TechnicalError, VersionStatusType?>> {
        return apolloClient.query(
            AppUpdateAvailabilityStatusQuery(
                packageName = "com.foresko.gamenever",
                version = BuildConfig.VERSION_NAME
            )
        )
            .toFlowCatching()
            .mapRight { apolloOperationResult ->
                when (apolloOperationResult) {
                    is ApolloOperationResult.Failure -> {
                        FirebaseCrashlytics.getInstance()
                            .recordException(ApolloOperationFailed(apolloOperationResult))

                        VersionStatusType.UPDATE_NOT_AVAILABLE
                    }

                    is ApolloOperationResult.Success ->
                        apolloOperationResult.data.let { availabilityStatus ->
                            try {
                                VersionStatusType.valueOf(
                                    availabilityStatus.androidAppUpdateAvailabilityStatus?.name
                                        ?: "UPDATE_NOT_AVAILABLE"
                                )
                            } catch (ex: Exception) {
                                VersionStatusType.UPDATE_NOT_AVAILABLE
                            }
                        }
                }
            }.flowOn(Dispatchers.IO)
    }
}
