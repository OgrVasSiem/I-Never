package com.foresko.gamenever.core.premium

import java.math.BigDecimal

data class SubscriptionInfo(
    val currencyCode: String,
    val price: BigDecimal,
    val hasFreeTrial: Boolean
)