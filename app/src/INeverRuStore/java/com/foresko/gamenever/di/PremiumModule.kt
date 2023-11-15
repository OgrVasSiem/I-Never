package com.foresko.gamenever.di

import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
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
        queryDispatcher: QueryDispatcher,
        commandDispatcher: CommandDispatcher
    ): PremiumSynchronizationManager {
        return PremiumSynchronizationManager(
            networkStatusTracker = networkStatusTracker,
            queryDispatcher = queryDispatcher,
            commandDispatcher = commandDispatcher
        )
    }
}