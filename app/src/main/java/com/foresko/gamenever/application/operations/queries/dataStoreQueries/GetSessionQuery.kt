package com.foresko.gamenever.application.operations.queries.dataStoreQueries

import com.foresko.gamenever.application.core.query.Query
import com.foresko.gamenever.application.core.query.QueryHandler
import com.foresko.gamenever.dataStore.Session
import com.foresko.gamenever.dataStore.SessionDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

object GetSessionQuery : Query<Session?>
class GetSessionQueryHandler @Inject constructor(
    private val sessionDataStore: SessionDataStore
) : QueryHandler<GetSessionQuery, Session?> {

    override fun handle(query: GetSessionQuery): Flow<Session?> {
        return sessionDataStore.data.flowOn(Dispatchers.IO)
    }
}