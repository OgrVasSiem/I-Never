package com.foresko.gamenever.core.premium

import java.math.BigDecimal

data class SubscriptionInfo(
    val currencyCode: String,
    val price: BigDecimal,
    val hasFreeTrial: Boolean
) {
    companion object {
        fun from(
            productInfo: ProductInfo?
        ): SubscriptionInfo {
            val currencyCode = productInfo?.currency ?: "rub"

            val price = productInfo?.microsPrice?.toBigDecimal()
                ?.divide(BigDecimal(1000000), 2, RoundingMode.HALF_DOWN) ?: BigDecimal.ZERO

            val hasFreeTrial = productInfo?.subFreeTrialPeriod != null

            return SubscriptionInfo(currencyCode, price, hasFreeTrial)
        }
    }
}
