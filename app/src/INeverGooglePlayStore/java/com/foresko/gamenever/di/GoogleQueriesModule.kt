package com.foresko.gamenever.di

import com.foresko.gamenever.application.core.query.QueryHandler
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetProductDetailsQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetProductDetailsQueryHandler
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetPurchasesSubscribeQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetPurchasesSubscribeQueryHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class GoogleQueriesModule {

    @Binds
    @IntoMap
    @QueriesModule.QueryHandlerKey(GetProductDetailsQuery::class)
    abstract fun bindGetProductDetailsQueryHandler(
        handler: GetProductDetailsQueryHandler
    ): QueryHandler<*, *>

    @Binds
    @IntoMap
    @QueriesModule.QueryHandlerKey(GetPurchasesSubscribeQuery::class)
    abstract fun bindGetPurchasesSubscribeQueryHandler(
        handler: GetPurchasesSubscribeQueryHandler
    ): QueryHandler<*, *>
}