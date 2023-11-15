package com.foresko.gamenever.di

import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetPremiumQuery
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetPremiumQueryHandler
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass
import com.foresko.gamenever.application.core.query.Query
import com.foresko.gamenever.application.core.query.QueryHandler
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQuery
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQueryHandler
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetAppUpdateAvailabilityStatusQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetAppUpdateAvailabilityStatusQueryHandler
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionPurchaseQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionPurchaseQueryHandler
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionQueryHandler
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionsQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionsQueryHandler
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInfoAboutUserSubscriptionQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInfoAboutUserSubscriptionQueryHandler

@Module
@InstallIn(SingletonComponent::class)
abstract class QueriesModule {

    @MapKey
    annotation class QueryHandlerKey(val value: KClass<out Query<*>>)
    @Binds
    @IntoMap
    @QueryHandlerKey(GetPremiumQuery::class)
    abstract fun bindGetPremiumQueryHandler(
        handler: GetPremiumQueryHandler
    ): QueryHandler<*, *>

    @Binds
    @IntoMap
    @QueryHandlerKey(GetInAppSubscriptionQuery::class)
    abstract fun bindGetInAppSubscriptionQueryHandler(
        getInAppSubscriptionQueryHandler: GetInAppSubscriptionQueryHandler
    ): QueryHandler<*, *>

    @Binds
    @IntoMap
    @QueryHandlerKey(GetInAppSubscriptionsQuery::class)
    abstract fun bindGetInAppSubscriptionsQueryHandler(
        getInAppSubscriptionsQueryHandler: GetInAppSubscriptionsQueryHandler
    ): QueryHandler<*, *>

    @Binds
    @IntoMap
    @QueryHandlerKey(GetInfoAboutUserSubscriptionQuery::class)
    abstract fun bindGetInfoAboutUserSubscriptionQueryHandler(
        GetInfoAboutUserSubscriptionQueryHandler: GetInfoAboutUserSubscriptionQueryHandler
    ): QueryHandler<*, *>

    @Binds
    @IntoMap
    @QueryHandlerKey(GetAppUpdateAvailabilityStatusQuery::class)
    abstract fun bindGetAppUpdateAvailabilityStatusQueryHandler(
        getAppUpdateAvailabilityStatusQueryHandler: GetAppUpdateAvailabilityStatusQueryHandler
    ): QueryHandler<*, *>

    @Binds
    @IntoMap
    @QueryHandlerKey(GetInAppSubscriptionPurchaseQuery::class)
    abstract fun bindGetInAppSubscriptionPurchaseQueryHandler(
        getInAppSubscriptionPurchaseQueryHandler: GetInAppSubscriptionPurchaseQueryHandler
    ): QueryHandler<*, *>

    @Binds
    @IntoMap
    @QueryHandlerKey(GetSessionQuery::class)
    abstract fun bindGetSessionQueryHandler(
        handler: GetSessionQueryHandler
    ): QueryHandler<*, *>


}