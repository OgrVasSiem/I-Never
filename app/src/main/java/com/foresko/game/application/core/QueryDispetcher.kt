package com.foresko.game.application.core

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class QueryDispatcher @Inject constructor(
    private val queryHandlers: Map<Class<out Query<*>>, @JvmSuppressWildcards QueryHandler<*, *>>
) {

    @Suppress("UNCHECKED_CAST")
    fun <QUERY : Query<RESULT>, RESULT> dispatch(query: QUERY): Flow<RESULT> {
        val queryHandlers = checkNotNull(
            queryHandlers[query::class.java] as? QueryHandler<QUERY, RESULT>
        ) {
            "Query handler for $query is not provided"
        }

        return queryHandlers.handle(query)
    }
}