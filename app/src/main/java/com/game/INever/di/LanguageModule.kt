package com.game.INever.di

import dagger.hilt.InstallIn
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
@Module
@InstallIn(SingletonComponent::class)
object LanguageModule {

    @Singleton
    @Provides
    fun provideLanguageManager(
        applicationLanguageDataStore: ApplicationLanguageDataStore,
        systemRegionDataStore: SystemRegionDataStore
    ): LanguageManager {
        return LanguageManager(
            applicationLanguageDataStore = applicationLanguageDataStore,
            systemRegionDataStore = systemRegionDataStore
        )
    }
}*/
