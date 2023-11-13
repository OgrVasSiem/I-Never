package com.foresko.gamenever.di

import android.content.Context
import com.foresko.gamenever.core.google.googleBilling.BillingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingClientModule {

    @Singleton
    @Provides
    fun provideBillingClientWrapper(
        @ApplicationContext context: Context
    ): BillingClient {
        return BillingClient(
            context = context
        )
    }
}