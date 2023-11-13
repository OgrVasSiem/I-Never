package com.foresko.gamenever.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

data class OnboardingState(
    val default: Boolean,
    val premium: Boolean
)

class ShowOnboardingDataStore(private val preferencesDataStore: DataStore<Preferences>) :
    DataStore<OnboardingState> {

    companion object {
        val nameKey1 = booleanPreferencesKey("default")
        val nameKey2 = booleanPreferencesKey("premium")
    }

    override val data: Flow<OnboardingState>
        get() = preferencesDataStore.data.map {
            val default = it[nameKey1] ?: true
            val premium = it[nameKey2] ?: true

            OnboardingState(default = default, premium = premium)
        }

    override suspend fun updateData(transform: suspend (t: OnboardingState) -> OnboardingState): OnboardingState {
        return withContext(Dispatchers.IO) {
            val updateData = preferencesDataStore.updateData {
                val default = it[nameKey1] ?: true
                val premium = it[nameKey2] ?: true

                val onboardingState = OnboardingState(default = default, premium = premium)

                preferencesOf(
                    nameKey1 to transform(onboardingState).default,
                    nameKey2 to transform(onboardingState).premium
                )
            }

            OnboardingState(
                updateData[nameKey1] ?: true,
                updateData[nameKey2] ?: true
            )
        }
    }
}