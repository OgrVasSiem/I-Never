package com.foresko.gamenever.ui.destinations.premium

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.amplitude.api.Amplitude
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.core.readModels.InAppSubscription
import com.foresko.gamenever.application.operations.commands.subscriptionCommand.PurchaseGoogleSubscribeCommand
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetPremiumQuery
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetInAppSubscriptionsQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetProductDetailsQuery
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetPurchasesSubscribeQuery
import com.foresko.gamenever.core.apollo.TechnicalError
import com.foresko.gamenever.core.utils.emptyString
import com.foresko.gamenever.dataStore.OnboardingState
import com.foresko.gamenever.dataStore.PremiumDataStore
import com.foresko.gamenever.dataStore.ShowOnboardingDataStore
import com.foresko.gamenever.ui.destination.premium.TariffType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val showOnboardingDataStore: ShowOnboardingDataStore,
    private val queryDispatcher: QueryDispatcher,
    private val premiumDataStore: PremiumDataStore,
    private val commandDispatcher: CommandDispatcher,
    @ApplicationContext applicationContext: Context
) : ViewModel() {
    var account by mutableStateOf(GoogleSignIn.getLastSignedInAccount(applicationContext))
        private set

    var premiumEndDateInEpochMilli by mutableStateOf(0L)
        private set

    var premiumIsActive by mutableStateOf(false)
        private set

    var onboardingState by mutableStateOf<OnboardingState?>(null)
        private set

    var tariffType by mutableStateOf(TariffType.Year)
        private set

    var inAppSubscriptions by mutableStateOf<List<InAppSubscription>>(emptyList())

    var subscriptionId by mutableStateOf(emptyString)
        private set

    fun changeTariffType(tariffType: TariffType) {
        this.tariffType = tariffType
        subscriptionId = inAppSubscriptions
            .firstOrNull { it.subscriptionName == tariffType.SBPSubscribeName }?.id ?: emptyString
    }

    var isNetworkConnectionError by mutableStateOf(false)
        private set

    fun changeNetworkConnectionErrorState(isNetworkConnectionError: Boolean) {
        this.isNetworkConnectionError = isNetworkConnectionError
    }

    private var _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails = _productDetails.asStateFlow()


    private var _purchasesSubscribe = MutableStateFlow<List<Purchase>>(emptyList())
    val purchasesSubscribe = _purchasesSubscribe.asStateFlow()

    fun buyGoogleSubscription(
        productDetails: ProductDetails,
        activityRef: WeakReference<Activity>,
        tag: String
    ) {
        viewModelScope.launch {
            commandDispatcher.dispatch(
                PurchaseGoogleSubscribeCommand(
                    productDetails = productDetails,
                    activityRef = activityRef,
                    tag = tag
                )
            )
        }
    }

    init {
        viewModelScope.launch {
            showOnboardingDataStore.data.collectLatest {
                onboardingState = it
            }
        }

        viewModelScope.launch {
            queryDispatcher.dispatch(GetPremiumQuery).collectLatest {
                premiumEndDateInEpochMilli = it.expiryDateTime
                premiumIsActive = it.isActive
            }
        }

        viewModelScope.launch {
            premiumDataStore.data.collectLatest {
                premiumEndDateInEpochMilli = it.expiryDateTime
                appOpenedEvent(it.isActive)

                premiumIsActive = it.isActive
            }
        }

        viewModelScope.launch {
            if (!premiumIsActive) {
                queryDispatcher.dispatch(GetInAppSubscriptionsQuery)
                    .collectLatest { subscriptions ->
                        when (subscriptions) {
                            is Either.Left -> {
                                if (subscriptions.value is TechnicalError.NetworkConnectionError) {
                                    delay(3000)

                                    changeNetworkConnectionErrorState(true)
                                }
                            }

                            is Either.Right -> {
                                inAppSubscriptions = subscriptions.value
                                subscriptionId = subscriptions.value
                                    .firstOrNull { it.subscriptionName == tariffType.SBPSubscribeName }?.id
                                    ?: emptyString
                            }
                        }
                    }
            }
        }

        viewModelScope.launch {
            queryDispatcher.dispatch(GetSessionQuery).collectLatest {
                account = GoogleSignIn.getLastSignedInAccount(applicationContext)
            }
        }

        viewModelScope.launch {
            queryDispatcher.dispatch(GetProductDetailsQuery).collectLatest { productDetails ->
                _productDetails.value = productDetails
            }
        }

        viewModelScope.launch {
            queryDispatcher.dispatch(GetPurchasesSubscribeQuery)
                .collectLatest { purchasesSubscribe ->
                    _purchasesSubscribe.value = purchasesSubscribe
                }
        }
    }

    private fun appOpenedEvent(premiumIsActive: Boolean) {
        val eventProperties = JSONObject()
        try {
            val info = if (!premiumIsActive) "free_user" else "premium_user"

            eventProperties.put("premium", info)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }

        Amplitude.getInstance().logEvent("app_opened", eventProperties)
    }
}