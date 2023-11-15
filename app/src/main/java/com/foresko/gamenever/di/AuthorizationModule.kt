package com.foresko.gamenever.di

import com.foresko.gamenever.core.google.AuthResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthorizationModule {

    @Singleton
    @Provides
    fun provideAuthResult(): AuthResult {
        return AuthResult()
    }
}