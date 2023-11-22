package com.foresko.gamenever.di

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.Executable
import com.apollographql.apollo3.cache.normalized.api.*
import com.apollographql.apollo3.cache.normalized.doNotStore
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.okHttpClient
import com.foresko.gamenever.application.operations.commands.authorizationCommands.RefreshAccessTokenCommand
import com.foresko.gamenever.application.operations.commands.authorizationCommands.RefreshAccessTokenCommandHandlerImpl
import com.foresko.gamenever.application.operations.commands.authorizationCommands.RefreshAccessTokenCommandResult
import com.foresko.gamenever.application.operations.commands.authorizationCommands.UnauthenticatedApolloClient
import com.foresko.gamenever.core.apollo.adapters.DateTimeAdapter
import com.foresko.gamenever.core.apollo.adapters.DecimalAdapter
import com.foresko.gamenever.dataStore.SessionDataStore
import com.foresko.gamenever.graphql.type.DateTime
import com.foresko.gamenever.graphql.type.Decimal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {
    @Provides
    @Singleton
    @UnauthenticatedApolloClient
    fun provideUnauthenticatedApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .httpServerUrl("https://api.nevergame.foresko.com/v1/graphql")
            .httpExposeErrorBody(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        sessionDataStore: SessionDataStore,
        refreshAccessTokenCommandHandler: RefreshAccessTokenCommandHandlerImpl
    ): ApolloClient {
        val authenticator = Authenticator { _, response ->
            if (response.count() > 3) return@Authenticator null

            runBlocking {
                val remoteCommandCallResult = refreshAccessTokenCommandHandler.handle(
                    RefreshAccessTokenCommand(
                        accessToken = response.request
                            .header("Authorization")
                            ?.removePrefix("Bearer ") ?: return@runBlocking null
                    )
                )

                when (remoteCommandCallResult) {
                    is Either.Left ->
                        null

                    is Either.Right ->
                        when (val commandResult = remoteCommandCallResult.value) {
                            is RefreshAccessTokenCommandResult.Success -> {
                                response
                                    .request
                                    .newBuilder()
                                    .header("Authorization", "Bearer ${commandResult.accessToken}")
                                    .build()
                            }

                            RefreshAccessTokenCommandResult.SessionNotFound -> null

                            is RefreshAccessTokenCommandResult.UnknownError -> null
                        }
                }
            }
        }

        val authenticationInterceptor = Interceptor { chain ->
            runBlocking {
                when (val accessToken = sessionDataStore.data.firstOrNull()?.accessToken) {
                    null -> chain.proceed(chain.request())

                    else -> {
                        val authenticatedRequest = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer $accessToken")
                            .build()

                        chain.proceed(authenticatedRequest)
                    }
                }
            }
        }


        val okHttpClient = OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(authenticationInterceptor)
            .build()

        val customScalarAdapters = CustomScalarAdapters.Builder()
            .add(Decimal.type, DecimalAdapter)
            .add(DateTime.type, DateTimeAdapter)
            .build()

        val cacheKeyGenerator = object : CacheKeyGenerator {

            override fun cacheKeyForObject(
                obj: Map<String, Any?>,
                context: CacheKeyGeneratorContext
            ): CacheKey? {
                return CacheKey(obj["id"] as? String ?: return null)
            }
        }

        val cacheResolver = object : CacheKeyResolver() {

            override fun cacheKeyForField(
                field: CompiledField,
                variables: Executable.Variables
            ): CacheKey? {
                val id = field.resolveArgument("id", variables)

                if (id is String) {
                    return CacheKey(id)
                }

                return null
            }
        }

        return ApolloClient.Builder()
            .httpServerUrl("https://api.nevergame.foresko.com/v1/graphql")
            .httpExposeErrorBody(true)
            .customScalarAdapters(customScalarAdapters)
            .okHttpClient(okHttpClient)
            .normalizedCache(
                normalizedCacheFactory = MemoryCacheFactory(),
                cacheKeyGenerator = cacheKeyGenerator,
                cacheResolver = cacheResolver
            )
            .doNotStore(true)
            .build()
    }

    private tailrec fun Response.count(): Int {
        return if (priorResponse == null) 1 else priorResponse!!.count()
    }
}