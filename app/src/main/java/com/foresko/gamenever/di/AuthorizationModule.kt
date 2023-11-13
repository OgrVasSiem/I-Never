package com.foresko.gamenever.di

import android.content.Context
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.core.google.AuthResult
import com.foresko.gamenever.core.google.AuthorizationManager
import com.foresko.gamenever.core.network.NetworkStatusTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthorizationModule {

    @Singleton
    @Provides
    fun provideAuthResult(
        commandDispatcher: CommandDispatcher
    ): AuthResult {
        return AuthResult(
            commandDispatcher = commandDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideAuthorizationManager(
        @ApplicationContext context: Context,
        networkStatusTracker: NetworkStatusTracker,
        commandDispatcher: CommandDispatcher
    ): AuthorizationManager {
        return AuthorizationManager(
            ioDispatcher = Dispatchers.IO,
            networkStatusTracker = networkStatusTracker,
            commandDispatcher = commandDispatcher,
            context = context
        )
    }
}