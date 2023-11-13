package com.foresko.gamenever.ui.destination.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.api.Amplitude
import com.foresko.gamenever.dataStore.FirstStartDataStore
import com.foresko.gamenever.dataStore.OnboardingState
import com.foresko.gamenever.dataStore.PremiumDataStore
import com.foresko.gamenever.dataStore.ShowOnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val showOnboardingDataStore: ShowOnboardingDataStore,
    private val premiumDataStore: PremiumDataStore,
    private val firstStartDataStore: FirstStartDataStore
) : ViewModel() {
    var premiumIsActive by mutableStateOf(false)
        private set

    var premiumEndDateInEpochMilli by mutableStateOf(0L)
        private set

    var onboardingState by mutableStateOf<OnboardingState?>(null)
        private set

    init {
        viewModelScope.launch {
            showOnboardingDataStore.data.collectLatest {
                onboardingState = it
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
            firstStartEvent()
        }
    }

    private suspend fun firstStartEvent() {
        if (firstStartDataStore.data.firstOrNull() == true) {
            Amplitude.getInstance().logEvent("first_start")

            firstStartDataStore.updateData { false }
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