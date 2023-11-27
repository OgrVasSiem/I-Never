package com.foresko.gamenever.ui.destinations.premium

import com.android.billingclient.api.ProductDetails
import java.math.BigDecimal
import java.math.RoundingMode

data class SubscriptionInfo(
    val currencyCode: String,
    val price: BigDecimal,
    val hasFreeTrial: Boolean
) {
    companion object {
        fun from(
            basicPurchaseDetails: ProductDetails?,
            basePlanId: String
        ): SubscriptionInfo {
            val offerDetails =
                basicPurchaseDetails?.subscriptionOfferDetails?.firstOrNull {
                    it.basePlanId.contains(
                        basePlanId
                    )
                }
            val pricePhase =
                offerDetails?.pricingPhases?.pricingPhaseList?.firstOrNull { it.priceAmountMicros > 0L }

            val currencyCode = pricePhase?.priceCurrencyCode ?: ""

            val price = pricePhase?.priceAmountMicros?.toBigDecimal()
                ?.divide(BigDecimal(1000000), 2, RoundingMode.HALF_DOWN) ?: BigDecimal.ZERO

            val freeTrialPhase =
                offerDetails?.pricingPhases?.pricingPhaseList?.firstOrNull { it.priceAmountMicros == 0L }

            val hasFreeTrial = freeTrialPhase != null

            return SubscriptionInfo(currencyCode, price, hasFreeTrial)
        }
    }
}