package com.foresko.game.di

import com.foresko.game.core.google.BillingClientWrapper
import com.foresko.game.core.network.NetworkStatusTracker
import com.foresko.game.core.premium.PremiumSynchronizationManager
import com.foresko.game.dataStore.PremiumDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PremiumModule {

    @Singleton
    @Provides
    fun providePremiumSynchronizationManager(
        premiumDataStore: PremiumDataStore,
        networkStatusTracker: NetworkStatusTracker,
        billingClientWrapper: BillingClientWrapper
    ): PremiumSynchronizationManager {
        return PremiumSynchronizationManager(
            ioDispatcher = Dispatchers.IO,
            premiumDataStore = premiumDataStore,
            networkStatusTracker = networkStatusTracker,
            billingClientWrapper = billingClientWrapper
        )
    }
}