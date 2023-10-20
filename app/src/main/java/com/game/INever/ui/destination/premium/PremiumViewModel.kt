package com.game.INever.ui.destination.premium

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.api.Amplitude
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.game.INever.core.google.BillingClientWrapper
import com.game.INever.core.premium.TariffType
import com.game.INever.dataStore.OnboardingState
import com.game.INever.dataStore.PremiumDataStore
import com.game.INever.dataStore.ShowOnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val premiumDataStore: PremiumDataStore,
    private val showOnboardingDataStore: ShowOnboardingDataStore,
    private val billingClientWrapper: BillingClientWrapper
) : ViewModel() {
    var onboardingState by mutableStateOf<OnboardingState?>(null)
        private set

    var premiumEndDateInEpochMilli by mutableStateOf(0L)
        private set

    var tariffType by mutableStateOf(TariffType.ThreeMonth)
        private set

    fun changeTariffType(tariffType: TariffType) {
        this.tariffType = tariffType
    }

    var premiumIsActive by mutableStateOf<Boolean?>(null)
        private set

    val subscribeForSaleFlows = billingClientWrapper.basicSubscribeDetails
    private val purchasesSubscribe = billingClientWrapper.purchasesSubscribe

    fun buy(
        productDetails: ProductDetails,
        activity: Activity,
        tag: String
    ) {
        val offers =
            productDetails.subscriptionOfferDetails?.let {
                retrieveEligibleOffers(
                    offerDetails = it,
                    tag = tag
                )
            }
        val offerToken = offers?.let { leastPricedOfferToken(it) }

        if (purchasesSubscribe.value.isEmpty()) {
            val billingParams = offerToken?.let {
                subscribeBillingFlowParamsBuilder(
                    productDetails = productDetails,
                    offerToken = it
                )
            }

            if (billingParams != null) {
                billingClientWrapper.launchBillingFlow(
                    activity,
                    billingParams.build()
                )
            }
        } else {
            Log.e("PremiumViewModel.GoogleSubscriptions", "User has more than 1 current purchase.")
        }
    }

    init {
        Amplitude.getInstance().logEvent("premium_screen")

        viewModelScope.launch {
            showOnboardingDataStore.data.collectLatest { onboarding ->
                onboardingState = onboarding

                showOnboardingDataStore.updateData { onboarding.copy(default = false) }
            }
        }

        viewModelScope.launch {
            premiumDataStore.data.collectLatest {
                premiumEndDateInEpochMilli = it.expiryDateTime
                premiumIsActive = it.isActive
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