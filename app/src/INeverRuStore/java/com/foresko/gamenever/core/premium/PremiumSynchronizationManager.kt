package com.foresko.gamenever.core.premium

import android.util.Log
import arrow.core.Either
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.operations.commands.dataStoreCommand.UpdatePremiumCommand
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInfoAboutUserSubscriptionQuery
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.network.NetworkStatus
import com.foresko.gamenever.core.network.NetworkStatusTracker
import com.foresko.gamenever.dataStore.Premium
import com.foresko.gamenever.dataStore.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PremiumSynchronizationManager(
    private val networkStatusTracker: NetworkStatusTracker,
    private val queryDispatcher: QueryDispatcher,
    private val commandDispatcher: CommandDispatcher
) {
    val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun init() {
        coroutineScope.launch {
            networkStatusTracker.networkStatus.flatMapLatest { networkStatus ->
                getPremiumInfo(networkStatus)
            }.debounce(300).collectLatest { subscriptionInfo ->
                if (subscriptionInfo.networkStatus is NetworkStatus.Available) {
                    when {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getPremiumInfo(
        networkStatus: NetworkStatus
    ): Flow<PremiumInfo<Either<TechnicalError, Premium?>, NetworkStatus, Session?>> {
        return queryDispatcher.dispatch(GetSessionQuery).flatMapLatest { session ->
            queryDispatcher.dispatch(GetInfoAboutUserSubscriptionQuery)
                .map { sbpSubscriptionInfo ->
                    PremiumInfo(
                        sbpSubscriptionInfo,
                        networkStatus,
                        session
                    )
                }
        }
    }

    private data class PremiumInfo<out A, out B, out C>(
        val sbpSubscriptionInfo: A,
        val networkStatus: B,
        val session: C
    )
}