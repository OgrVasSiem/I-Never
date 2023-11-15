package com.foresko.gamenever.core.premium

import android.util.Log
import arrow.core.Either
import com.android.billingclient.api.Purchase
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.operations.commands.dataStoreCommand.UpdatePremiumCommand
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInfoAboutUserSubscriptionQuery
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.google.googleBilling.BillingClient
import com.foresko.gamenever.core.network.NetworkStatus
import com.foresko.gamenever.core.network.NetworkStatusTracker
import com.foresko.gamenever.dataStore.Premium
import com.foresko.gamenever.dataStore.Session
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PremiumSynchronizationManager(
    private val networkStatusTracker: NetworkStatusTracker,
    private val billingClient: BillingClient,
    private val queryDispatcher: QueryDispatcher,
    private val commandDispatcher: CommandDispatcher
) {
    val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun init() {
        try {
            billingClient.startBillingConnection(coroutineScope = coroutineScope)
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().recordException(ex)
        }

        coroutineScope.launch {
            networkStatusTracker.networkStatus.flatMapLatest { networkStatus ->
                getPremiumInfo(networkStatus)
            }.debounce(300).collectLatest { subscriptionInfo ->
                if (subscriptionInfo.networkStatus is NetworkStatus.Available) {
                    when {
                        hasGoogleSubscription(subscriptionInfo.googleSubscription) -> {
                            setGooglePremium()
                        }

                        hasValidSession(subscriptionInfo.session) -> {
                            setSBPPremium(subscriptionInfo.sbpSubscriptionInfo)
                        }

                        else -> {
                            clearPremium()
                        }
                    }
                }
            }
        }
    }

    private fun hasGoogleSubscription(googleSubscription: List<Purchase>): Boolean =
        googleSubscription.isNotEmpty()

    private fun hasValidSession(session: Session?): Boolean = session != null

    private suspend fun clearPremium() {
        commandDispatcher.dispatch(UpdatePremiumCommand(Premium(false, 0L)))
    }

    private suspend fun setSBPPremium(
        sbpSubscriptionInfo: Either<TechnicalError, Premium?>
    ) {
        when (sbpSubscriptionInfo) {
            is Either.Left -> {
                Log.e("premium", "error = ${sbpSubscriptionInfo.value}")
            }

            is Either.Right -> {
                commandDispatcher.dispatch(UpdatePremiumCommand(sbpSubscriptionInfo.value))
            }
        }
    }

    private suspend fun setGooglePremium() {
        commandDispatcher.dispatch(
            UpdatePremiumCommand(
                Premium(
                    isActive = true,
                    expiryDateTime = 0L,
                )
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getPremiumInfo(
        networkStatus: NetworkStatus
    ): Flow<PremiumInfo<List<Purchase>, Either<TechnicalError, Premium?>, NetworkStatus, Session?>> {
        return combine(
            billingClient.purchasesSubscribe,
            queryDispatcher.dispatch(GetSessionQuery).flatMapLatest { session ->
                queryDispatcher.dispatch(GetInfoAboutUserSubscriptionQuery)
                    .map { it to session }
            }
        ) { googleSubscription, sbpSubscription ->
            val session = sbpSubscription.second
            val sbpSubscriptionInfo = sbpSubscription.first

            PremiumInfo(
                googleSubscription,
                sbpSubscriptionInfo,
                networkStatus,
                session
            )
        }
    }

    private data class PremiumInfo<out A, out B, out C, out D>(
        val googleSubscription: A,
        val sbpSubscriptionInfo: B,
        val networkStatus: C,
        val session: D
    )
}