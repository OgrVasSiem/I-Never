package com.foresko.gamenever.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

data class Session(val accessToken: String, val refreshToken: String)

class SessionDataStore(
    private val preferencesDataStore: DataStore<Preferences>
) : DataStore<Session?> {
    companion object {

        private val accessTokenPreferenceKey = stringPreferencesKey("key_access_token")

        private val refreshTokenPreferenceKey = stringPreferencesKey("key_refresh_token")
    }

    private val mutex = Mutex()

    override val data: Flow<Session?>
        get() = preferencesDataStore.data.map { preferences -> preferences.toSession() }

    override suspend fun updateData(transform: suspend (t: Session?) -> Session?): Session? {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val updatedPreferences = preferencesDataStore.updateData { preferences ->
                        preferences
                            .toSession()
                            .run { transform(this) }
                            .toPreferences()
                    }

                    updatedPreferences.toSession()
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                    return@withContext data.first()
                }
            }
        }
    }

    private fun Preferences.toSession(): Session? {
        val accessToken = this[accessTokenPreferenceKey]
        val refreshToken = this[refreshTokenPreferenceKey]

        return when {
            accessToken == null && refreshToken == null -> null

            else -> Session(
                accessToken = accessToken!!,
                refreshToken = refreshToken!!
            )
        }
    }

    private fun Session?.toPreferences(): Preferences {
        return when (this) {
            null -> emptyPreferences()
            else -> Preferences {
                this[accessTokenPreferenceKey] = accessToken
                this[refreshTokenPreferenceKey] = refreshToken
            }
        }
    }

    private inline fun Preferences(builder: MutablePreferences.() -> Unit): Preferences {
        return mutablePreferencesOf().apply(builder)
    }
}