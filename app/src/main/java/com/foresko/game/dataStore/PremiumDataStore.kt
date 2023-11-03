package com.foresko.game.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

data class Premium (val isActive: Boolean, val expiryDateTime: Long)

class PremiumDataStore (private val preferencesDataStore: DataStore<Preferences>) :
    DataStore<Premium> {

    companion object {
        val isActiveKey = booleanPreferencesKey("isActive")
        val expiryDateTimeKey = longPreferencesKey("expiryDateTime")
    }

    private val mutex = Mutex()

    override val data: Flow<Premium>
        get() = preferencesDataStore.data.map {
            val isActive = it[isActiveKey] ?: false
            val expiryDateTime = it[expiryDateTimeKey] ?: 0L

            Premium(isActive = isActive, expiryDateTime = expiryDateTime)
        }

    override suspend fun updateData(transform: suspend (t: Premium) -> Premium): Premium {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val updateData = preferencesDataStore.updateData {
                        val isActive = it[isActiveKey] ?: false

                        val expiryDateTime = it[expiryDateTimeKey] ?: 0L

                        val premium =
                            Premium(isActive = isActive, expiryDateTime = expiryDateTime)

                        preferencesOf(
                            isActiveKey to transform(premium).isActive,
                            expiryDateTimeKey to transform(premium).expiryDateTime
                        )
                    }

                    Premium(
                        updateData[isActiveKey] ?: false,
                        updateData[expiryDateTimeKey] ?: 0L
                    )
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                    return@withContext data.first()
                }
            }
        }
    }
}