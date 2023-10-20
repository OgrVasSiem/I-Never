package com.game.INever.core.google

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BillingClientWrapper(
    context: Context
) : PurchasesUpdatedListener {
    private val subscribeWithProductDetails =
        MutableStateFlow<Map<String, ProductDetails>>(emptyMap())

    private var _purchasesSubscribe = MutableStateFlow<List<Purchase>>(listOf())

    val purchasesSubscribe = _purchasesSubscribe.asStateFlow()

    private val _isNewPurchaseAcknowledged = MutableStateFlow(value = false)

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    fun startBillingConnection(
        coroutineScope: CoroutineScope
    ) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing response OK")

                    querySubscribePurchases()
                    coroutineScope.launch(Dispatchers.IO) { querySubscribeDetails() }
                } else {
                    Log.e(TAG, billingResult.debugMessage)
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.i(TAG, "Billing connection disconnected")
                startBillingConnection(coroutineScope)
            }
        })
    }

    private fun querySubscribePurchases() {
        if (!billingClient.isReady) Log.e(TAG, "queryPurchases: BillingClient is not ready")

        billingClient.queryPurchasesAsync(
            QueryPurchasesParams
                .newBuilder()
                .setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (purchaseList.isNotEmpty()) {
                    _purchasesSubscribe.value = purchaseList
                } else {
                    _purchasesSubscribe.value = emptyList()
                }

            } else {
                Log.e(TAG, billingResult.debugMessage)
            }
        }
    }

    private suspend fun querySubscribeDetails() {
        val params = QueryProductDetailsParams.newBuilder()
        val productList = mutableListOf<QueryProductDetailsParams.Product>()
        for (product in LIST_OF_SUBSCRIBE) {

            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(product)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )

            params.setProductList(productList).let { productDetailsParams ->
                Log.i(TAG, "querySubscribeDetailsAsync")

                subscribeWithProductDetails.value = billingClient
                    .queryProductDetails(productDetailsParams.build()).productDetailsList?.let {
                        it.associateBy { productDetails ->
                            productDetails.productId
                        }
                    } ?: emptyMap()
            }
        }
    }

    fun launchBillingFlow(activity: Activity, params: BillingFlowParams) {
        if (!billingClient.isReady) {
            Log.e(TAG, "launchBillingFlow: BillingClient is not ready")
        }
        billingClient.launchBillingFlow(activity, params)
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
            && !purchases.isNullOrEmpty()
        ) {
            _purchasesSubscribe.value = purchases

            for (purchase in purchases) {
                acknowledgePurchases(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.e(TAG, "User has cancelled")
        } else {
            Log.d(TAG, "Other error")
        }
    }

    private fun acknowledgePurchases(purchase: Purchase?) {
        purchase?.let {
            if (!it.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(
                    params
                ) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                        it.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        _isNewPurchaseAcknowledged.value = true
                    }
                }
            }
        }
    }

    val basicSubscribeDetails: Flow<ProductDetails> =
        subscribeWithProductDetails
            .filter { it.containsKey(BASIC_SUB) }
            .map { it[BASIC_SUB]!! }

    companion object {
        private const val TAG = "BillingClient"

        private const val BASIC_SUB = "subscribe"

        private val LIST_OF_SUBSCRIBE = listOf(BASIC_SUB)
    }
}