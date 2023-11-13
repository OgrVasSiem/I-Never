package com.foresko.gamenever.application.operations.queries.subscriptionQueries

import com.android.billingclient.api.ProductDetails
import com.foresko.gamenever.application.core.query.Query
import com.foresko.gamenever.application.core.query.QueryHandler
import com.foresko.gamenever.core.google.googleBilling.BillingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

object GetProductDetailsQuery : Query<ProductDetails>

class GetProductDetailsQueryHandler @Inject constructor(
    private val billingClient: BillingClient
) : QueryHandler<GetProductDetailsQuery, ProductDetails> {

    override fun handle(query: GetProductDetailsQuery): Flow<ProductDetails> {
        return billingClient.productDetails.flowOn(Dispatchers.IO)
    }
}