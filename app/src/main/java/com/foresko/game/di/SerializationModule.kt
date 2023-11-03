package com.foresko.game.di

import com.foresko.game.core.rest.CardsRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://never-game.hb.ru-msk.vkcs.cloud")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePurchaseSubscriptionInfo(retrofit: Retrofit): CardsRequest {
        return retrofit.create(CardsRequest::class.java)
    }
}
