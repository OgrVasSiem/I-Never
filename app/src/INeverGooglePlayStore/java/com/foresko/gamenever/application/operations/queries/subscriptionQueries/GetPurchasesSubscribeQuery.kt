package com.foresko.gamenever.application.operations.queries.subscriptionQueries

import com.android.billingclient.api.Purchase
import com.foresko.gamenever.application.core.query.Query
import com.foresko.gamenever.application.core.query.QueryHandler
import com.foresko.gamenever.core.google.googleBilling.BillingClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

object GetPurchasesSubscribeQuery : Query<List<Purchase>>

class GetPurchasesSubscribeQueryHandler @Inject constructor(
    private val billingClient: BillingClient
) : QueryHandler<GetPurchasesSubscribeQuery, List<Purchase>> {

    override fun handle(query: GetPurchasesSubscribeQuery): Flow<List<Purchase>> {
        return billingClient.purchasesSubscribe
    }
}