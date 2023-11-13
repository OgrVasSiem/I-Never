package com.foresko.gamenever.ui.destination.premium.pendingStatusPurchaseScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.operations.commands.dataStoreCommand.UpdatePremiumCommand
import com.foresko.gamenever.application.operations.commands.subscriptionCommand.PurchaseInAppSubscriptionCommand
import com.foresko.gamenever.application.operations.commands.subscriptionCommand.PurchaseInAppSubscriptionCommandResult
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionPurchaseQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInfoAboutUserSubscriptionQuery
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.utils.emptyString
import com.foresko.gamenever.ui.destinations.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PendingStatusPurchaseViewModel @Inject constructor(
    private val queryDispatcher: QueryDispatcher,
    private val commandDispatcher: CommandDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val subscriptionId = savedStateHandle.navArgs<PendingStatusPurchaseNavArgs>().subscriptionId

    var confirmationUrl by mutableStateOf(emptyString)
        private set

    var inAppSubscriptionPurchaseId by mutableStateOf(emptyString)
        private set

    var isNetworkConnectionError by mutableStateOf(false)
        private set

    fun changeNetworkConnectionErrorState(isNetworkConnectionError: Boolean) {
        this.isNetworkConnectionError = isNetworkConnectionError
    }

    var isServerError by mutableStateOf(false)
        private set

    fun changeServerErrorState(isServerError: Boolean) {
        this.isServerError = isServerError
    }

    var purchaseStatus by mutableStateOf<PurchaseStatus?>(null)
        private set

    init {
        viewModelScope.launch {
            val result = commandDispatcher.dispatch(
                PurchaseInAppSubscriptionCommand(subscriptionId = subscriptionId)
            )

            when (result) {
                is Either.Left -> {
                    if (result.value is TechnicalError.NetworkConnectionError) {
                        changeNetworkConnectionErrorState(true)
                    } else {
                        changeServerErrorState(true)
                    }
                }

                is Either.Right -> {
                    when (val commandResult = result.value) {
                        is PurchaseInAppSubscriptionCommandResult.Success -> {
                            inAppSubscriptionPurchaseId = commandResult.id
                            confirmationUrl = commandResult.confirmationUrl
                        }

                        is PurchaseInAppSubscriptionCommandResult.UnknownError -> {
                            Log.e(
                                "PendingStatusPurchase.Purchase",
                                "unknownError = ${commandResult.message}"
                            )

                            changeServerErrorState(true)
                        }
                    }
                }
            }
        }

        snapshotFlow { inAppSubscriptionPurchaseId }.onEach {
            if (it.isNotEmpty()) {
                while (true) {
                    delay(3000)

                    queryDispatcher.dispatch(GetInAppSubscriptionPurchaseQuery(id = inAppSubscriptionPurchaseId))
                        .collectLatest { result ->
                            when (result) {
                                is Either.Left -> {
                                    Log.e(
                                        "pendingStatusPurchaseViewModel.status",
                                        "technicalError = ${result.value}}"
                                    )
                                }

                                is Either.Right -> {
                                    purchaseStatus =
                                        when (result.value ?: PurchaseStatus.Canceled) {
                                            PurchaseStatus.Pending -> {
                                                PurchaseStatus.Pending
                                            }

                                            PurchaseStatus.Confirmed -> {
                                                withContext(NonCancellable) {
                                                    val resultViewer = queryDispatcher.dispatch(
                                                        GetInfoAboutUserSubscriptionQuery
                                                    ).firstOrNull()

                                                    when (resultViewer) {
                                                        is Either.Left -> {
                                                            Log.e(
                                                                "pendingStatusPurchaseViewModel.viewer",
                                                                "technicalError = ${result.value}}"
                                                            )
                                                        }

                                                        is Either.Right -> {
                                                            commandDispatcher.dispatch(
                                                                UpdatePremiumCommand(resultViewer.value)
                                                            )
                                                        }

                                                        else -> {
                                                            Log.e(
                                                                "pendingStatusPurchaseViewModel.viewer",
                                                                "unknownError = ${result.value}}"
                                                            )
                                                        }
                                                    }
                                                }

                                                delay(2000)

                                                PurchaseStatus.Confirmed
                                            }

                                            PurchaseStatus.Canceled -> {
                                                delay(2000)

                                                PurchaseStatus.Canceled
                                            }
                                        }
                                }
                            }
                        }
                }
            }
        }.launchIn(viewModelScope)
    }
}