package com.foresko.gamenever.application.core.query

import kotlinx.coroutines.flow.Flow

fun interface QueryHandler<QUERY : Query<RESULT>, RESULT> {

    fun handle(query: QUERY): Flow<RESULT>
}