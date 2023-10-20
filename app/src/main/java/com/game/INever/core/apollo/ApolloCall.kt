package com.game.INever.core.apollo

import arrow.core.Either
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.*
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import com.apollographql.apollo3.api.json.jsonReader
import com.apollographql.apollo3.exception.ApolloCompositeException
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.game.INever.application.core.TechnicalError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


fun <D : Operation.Data> ApolloCall<D>.toFlowCatching(): Flow<Either<TechnicalError, ApolloOperationResult<D>>> {
    return toFlow()
        .map<ApolloResponse<D>, Either<TechnicalError, ApolloOperationResult<D>>> { apolloResponse ->
            ApolloOperationResult
                .valueOf(apolloResponse)
                .right()
        }
        .catch { cause -> emit(cause.toTechnicalError().left()) }
}

private fun Throwable.toTechnicalError(): TechnicalError = toTechnicalErrorOrNull() ?: throw this

private fun Throwable.toTechnicalErrorOrNull(): TechnicalError? = when (this) {
    is ApolloNetworkException ->
        TechnicalError.NetworkConnectionError(this)

    is ApolloHttpException -> {
        val messages = body
            ?.jsonReader()
            .toOption().flatMap { jsonReader ->
                Option.catch {
                    jsonReader.use {
                        val response =
                            ResponseAdapter
                                .obj()
                                .fromJson(jsonReader, CustomScalarAdapters.Empty)

                        response.errors.map { error -> error.message }
                    }
                }
            }
            .getOrElse { emptyList() }

        TechnicalError.HttpError(this, statusCode, messages)
    }

    is ApolloCompositeException -> {
        val firstException = suppressedExceptions[0] as Exception
        val secondException = suppressedExceptions[1] as Exception

        val first = firstException.toTechnicalErrorOrNull()
        val second = secondException.toTechnicalErrorOrNull()

        when {
            first is TechnicalError.NetworkConnectionError || first is TechnicalError.HttpError -> first
            second is TechnicalError.NetworkConnectionError || second is TechnicalError.HttpError -> second
            else -> TechnicalError.OtherError(this)
        }
    }

    is ApolloException ->
        TechnicalError.OtherError(this)

    else -> null
}

private data class Response(val errors: List<Error>) {

    data class Error(val message: String)
}

private object ResponseAdapter : Adapter<Response> {

    val RESPONSE_NAMES: List<String> = listOf("errors")

    override fun fromJson(
        reader: JsonReader,
        customScalarAdapters: CustomScalarAdapters
    ): Response {
        var response: List<Response.Error>? = null

        while (true) {
            when (reader.selectName(RESPONSE_NAMES)) {
                0 -> response = ErrorAdapter.obj().list().fromJson(reader, customScalarAdapters)
                else -> break
            }
        }

        return Response(errors = response!!)
    }

    override fun toJson(
        writer: JsonWriter,
        customScalarAdapters: CustomScalarAdapters,
        value: Response
    ) {
        throw UnsupportedOperationException()
    }
}

private object ErrorAdapter : Adapter<Response.Error> {

    val RESPONSE_NAMES: List<String> = listOf("message")

    override fun fromJson(
        reader: JsonReader,
        customScalarAdapters: CustomScalarAdapters
    ): Response.Error {
        var message: String? = null

        while (true) {
            when (reader.selectName(RESPONSE_NAMES)) {
                0 -> message = StringAdapter.fromJson(reader, customScalarAdapters)
                else -> break
            }
        }

        return Response.Error(message!!)
    }

    override fun toJson(
        writer: JsonWriter,
        customScalarAdapters: CustomScalarAdapters,
        value: Response.Error
    ) {
        throw UnsupportedOperationException()
    }
}