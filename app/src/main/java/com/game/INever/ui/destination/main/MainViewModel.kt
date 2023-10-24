package com.game.INever.ui.destination.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.api.Amplitude
import com.game.INever.core.rest.Cards
import com.game.INever.core.rest.CardsRequest
import com.game.INever.dataStore.PremiumDataStore
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val premiumDataStore: PremiumDataStore,
    private val providePurchaseSubscriptionInfo: CardsRequest,
) : ViewModel() {
    private var _offers = MutableStateFlow<List<Cards>?>(null)

    private var premiumEndDateInEpochMilli by mutableLongStateOf(0L)

    val offers = _offers.asStateFlow()

    init {
        viewModelScope.launch {
            getMortgages()
        }
    }

    private suspend fun getMortgages() {
        try {
            _offers.value = providePurchaseSubscriptionInfo.infoGet().topics
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    var premiumIsActive by mutableStateOf<Boolean?>(null)
        private set

    init {
        Amplitude.getInstance().logEvent("premium_screen")

        viewModelScope.launch {
            premiumDataStore.data.collectLatest {
                premiumEndDateInEpochMilli = it.expiryDateTime
                premiumIsActive = it.isActive
            }
        }
    }
}