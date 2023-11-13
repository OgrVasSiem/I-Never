package com.foresko.gamenever.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.foresko.gamenever.dataStore.FirstStartDataStore
import com.foresko.gamenever.dataStore.PremiumDataStore
import com.foresko.gamenever.dataStore.SessionDataStore
import com.foresko.gamenever.dataStore.ShowOnboardingDataStore
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

    @Singleton
    @Provides
    fun provideSessionDataStore(@ApplicationContext context: Context): SessionDataStore {
        return SessionDataStore(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("session") }
            )
        )
    }


}