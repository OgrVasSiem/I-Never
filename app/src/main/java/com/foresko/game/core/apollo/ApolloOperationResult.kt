package com.foresko.game.core.apollo

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException

sealed class ApolloOperationResult<D : Operation.Data> {

    companion object {

        fun <D : Operation.Data> valueOf(apolloResponse: ApolloResponse<D>): ApolloOperationResult<D> {
            return when {
                !apolloResponse.errors.isNullOrEmpty() -> Failure(
                    operation = apolloResponse.operation,
                    errors = apolloResponse.errors!!,
                )

                apolloResponse.data != null -> Success(
                    data = apolloResponse.data!!,
                )

                else -> throw ApolloException("The server did not return any data")
            }
        }
    }

    abstract fun asEither(): Either<Failure<D>, Success<D>>

    class Success<D : Operation.Data>(
        val data: D,
    ) :
        ApolloOperationResult<D>() {

        override fun asEither(): Either<Failure<D>, Success<D>> {
            return right()
        }
    }

    class Failure<D : Operation.Data>(
        val operation: Operation<D>,
        val errors: List<Error>,
    ) :
        ApolloOperationResult<D>() {

        override fun asEither(): Either<Failure<D>, Success<D>> {
            return left()
        }
    }
}