package com.foresko.gamenever.core.rest

import retrofit2.http.GET

interface CardsRequest {
    @GET("/v1/data/ru.json")
    suspend fun infoGet(): CardsResponse
}
