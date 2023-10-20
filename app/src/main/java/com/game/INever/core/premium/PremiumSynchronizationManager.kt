package com.game.INever.core.premium

import com.game.INever.core.google.BillingClientWrapper
import com.game.INever.core.network.NetworkStatus
import com.game.INever.core.network.NetworkStatusTracker
import com.game.INever.dataStore.Premium
import com.game.INever.dataStore.PremiumDataStore
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class PremiumSynchronizationManager(
    private val ioDispatcher: CoroutineDispatcher,
    private val networkStatusTracker: NetworkStatusTracker,
    private val premiumDataStore: PremiumDataStore,
    private val billingClientWrapper: BillingClientWrapper
) {
    private val purchasesSubscribe = billingClientWrapper.purchasesSubscribe

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun init() {
        val coroutineScope = CoroutineScope(ioDispatcher + SupervisorJob())

        coroutineScope.launch {
            networkStatusTracker.networkStatus.flatMapLatest { networkStatus ->
                combine(
                    purchasesSubscribe,
                    flowOf(networkStatus)
                ) { purchaseSubscription, status ->
                    Pair(
                        purchaseSubscription,
                        status
                    )
                }
            }.debounce(300).collectLatest { subscriptionInfo ->
                try {
                    billingClientWrapper.startBillingConnection(coroutineScope = coroutineScope)
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                    return@collectLatest
                }

                if (subscriptionInfo.second is NetworkStatus.Available) {
                    if (subscriptionInfo.first.isEmpty()) {
                        premiumDataStore.updateData { Premium(
                            isActive = false,
                            expiryDateTime = 0L
                        ) }
                    } else {
                        premiumDataStore.updateData { Premium(true, 0L) }
                    }
                }
            }
        }
    }
}