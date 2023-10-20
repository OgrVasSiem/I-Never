package com.game.INever.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.game.INever.dataStore.FirstStartDataStore
import com.game.INever.dataStore.PremiumDataStore
import com.game.INever.dataStore.ShowOnboardingDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providePremiumDataStore(@ApplicationContext context: Context): PremiumDataStore {
        return PremiumDataStore(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("premium") }
            )
        )
    }

    @Singleton
    @Provides
    fun provideShowOnboardingDataStore(@ApplicationContext context: Context): ShowOnboardingDataStore {
        return ShowOnboardingDataStore(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("showOnboarding") }
            )
        )
    }

    @Singleton
    @Provides
    fun provideFirstStartDataStore(@ApplicationContext context: Context): FirstStartDataStore {
        return FirstStartDataStore(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("firstStart") }
            )
        )
    }
}