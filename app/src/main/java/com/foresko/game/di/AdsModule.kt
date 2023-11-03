package com.foresko.game.di

import android.content.Context
import com.foresko.game.core.ads.Ads
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Singleton
    @Provides
    fun provideRewardAds(
        @ApplicationContext applicationContext: Context
    ): Ads {
        return Ads(
            context = applicationContext
        )
    }
}