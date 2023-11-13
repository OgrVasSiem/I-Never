package com.foresko.gamenever.di

import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.core.google.googleBilling.BillingClient
import com.foresko.gamenever.core.network.NetworkStatusTracker
import com.foresko.gamenever.core.premium.PremiumSynchronizationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PremiumModule {

    @Singleton
    @Provides
    fun providePremiumSynchronizationManager(
        networkStatusTracker: NetworkStatusTracker,
        billingClient: BillingClient,
        queryDispatcher: QueryDispatcher,
        commandDispatcher: CommandDispatcher
    ): PremiumSynchronizationManager {
        return PremiumSynchronizationManager(
            commandDispatcher = commandDispatcher,
            networkStatusTracker = networkStatusTracker,
            billingClient = billingClient,
            queryDispatcher = queryDispatcher
        )
    }
}