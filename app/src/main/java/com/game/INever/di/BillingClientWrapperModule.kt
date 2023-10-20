package com.game.INever.di

import android.content.Context
import com.game.INever.core.google.BillingClientWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingClientWrapperModule {

    @Singleton
    @Provides
    fun provideBillingClientWrapper(
        @ApplicationContext applicationContext: Context
    ): BillingClientWrapper {
        return BillingClientWrapper(
            context = applicationContext
        )
    }
}