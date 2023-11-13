package com.foresko.gamenever.application.operations.queries.dataStoreQueries

import com.foresko.gamenever.application.core.query.Query
import com.foresko.gamenever.application.core.query.QueryHandler
import com.foresko.gamenever.dataStore.Premium
import com.foresko.gamenever.dataStore.PremiumDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

object GetPremiumQuery : Query<Premium>

class GetPremiumQueryHandler @Inject constructor(
    private val premiumDataStore: PremiumDataStore
) : QueryHandler<GetPremiumQuery, Premium> {

    override fun handle(query: GetPremiumQuery): Flow<Premium> {
        return premiumDataStore.data.flowOn(Dispatchers.IO)
    }
}