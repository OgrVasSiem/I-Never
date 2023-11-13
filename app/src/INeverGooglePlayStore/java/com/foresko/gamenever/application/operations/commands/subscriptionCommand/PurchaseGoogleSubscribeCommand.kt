package com.foresko.gamenever.application.operations.commands.subscriptionCommand

import android.app.Activity
import android.util.Log

import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.foresko.gamenever.application.core.command.Command
import com.foresko.gamenever.application.core.command.CommandHandler
import com.foresko.gamenever.core.google.googleBilling.BillingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject

data class PurchaseGoogleSubscribeCommand(
    val productDetails: ProductDetails?,
    val activityRef: WeakReference<Activity>,
    val tag: String
) : Command<Unit>

class PurchaseGoogleSubscribeCommandHandler @Inject constructor(
    private val billingClient: BillingClient
) : CommandHandler<PurchaseGoogleSubscribeCommand, Unit> {

    override suspend fun handle(command: PurchaseGoogleSubscribeCommand) {
        val activity = command.activityRef.get()

        if (activity == null || activity.isFinishing) {
            return
        }

        withContext(Dispatchers.Main) {
            val offers =
                command.productDetails?.subscriptionOfferDetails?.let {
                    retrieveEligibleOffers(
                        offerDetails = it,
                        tag = command.tag
                    )
                }

            val offerToken = offers?.let { leastPricedOfferToken(it) }

            if (billingClient.purchasesSubscribe.value.isEmpty() && command.productDetails != null) {
                val billingParams = offerToken?.let {
                    subscribeBillingFlowParamsBuilder(
                        productDetails = command.productDetails,
                        offerToken = it
                    )
                }

                if (billingParams != null) {
                    billingClient.launchBillingFlow(
                        activity,
                        billingParams.build()
                    )
                } else {
                }
            } else {
            }
        }
    }

    private fun retrieveEligibleOffers(
        offerDetails: MutableList<ProductDetails.SubscriptionOfferDetails>,
        tag: String
    ): List<ProductDetails.SubscriptionOfferDetails> {
        val eligibleOffers = emptyList<ProductDetails.SubscriptionOfferDetails>().toMutableList()
        offerDetails.forEach { offerDetail ->
            if (offerDetail.offerTags.contains(tag)) {
                eligibleOffers.add(offerDetail)
            }
        }

        return eligibleOffers
    }

    private fun leastPricedOfferToken(
        offerDetails: List<ProductDetails.SubscriptionOfferDetails>
    ): String {
        var offerToken = String()
        var leastPricedOffer: ProductDetails.SubscriptionOfferDetails
        var lowestPrice = Int.MAX_VALUE

        if (offerDetails.isNotEmpty()) {
            for (offer in offerDetails) {
                for (price in offer.pricingPhases.pricingPhaseList) {
                    if (price.priceAmountMicros < lowestPrice) {
                        lowestPrice = price.priceAmountMicros.toInt()
                        leastPricedOffer = offer
                        offerToken = leastPricedOffer.offerToken
                    }
                }
            }
        }
        return offerToken
    }

    private fun subscribeBillingFlowParamsBuilder(
        productDetails: ProductDetails,
        offerToken: String
    ): BillingFlowParams.Builder {
        return BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        )
    }
}