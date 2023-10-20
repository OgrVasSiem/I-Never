package com.game.INever.application.core

sealed class TechnicalError {

    data class NetworkConnectionError(
        val exception: Exception
    ) :
        TechnicalError()

    data class HttpError(
        val exception: Exception,
        val statusCode: Int,
        val messages: List<String>
    ) :
        TechnicalError()

    data class OtherError(
        val exception: Exception
    ) :
        TechnicalError()
}