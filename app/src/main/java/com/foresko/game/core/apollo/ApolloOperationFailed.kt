package com.foresko.game.core.apollo

import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException

class ApolloOperationFailed(
    operation: Operation<*>,
    errors: List<Error>
) :
    ApolloException(messageFrom(operation, errors)) {

    constructor(failure: ApolloOperationResult.Failure<*>) : this(failure.operation, failure.errors)

    companion object {

        private fun messageFrom(operation: Operation<*>, errors: List<Error>): String {
            return "Operation $operation failed with following errors: ${errors.joinToString()}"
        }
    }
}