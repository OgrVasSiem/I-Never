package com.foresko.game.application.core

import kotlinx.coroutines.flow.Flow

fun interface QueryHandler<QUERY : Query<RESULT>, RESULT> {

    fun handle(query: QUERY): Flow<RESULT>
}