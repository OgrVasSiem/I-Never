package com.foresko.gamenever.application.core.readModels

import java.math.BigDecimal

data class InAppSubscription(
    val id: String,
    val price: BigDecimal,
    val currencyCode: String,
    val subscriptionName: String
)
