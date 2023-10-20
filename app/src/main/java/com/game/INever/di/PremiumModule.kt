package com.game.INever.di

import com.game.INever.core.google.BillingClientWrapper
import com.game.INever.core.network.NetworkStatusTracker
import com.game.INever.core.premium.PremiumSynchronizationManager
import com.game.INever.dataStore.PremiumDataStore
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