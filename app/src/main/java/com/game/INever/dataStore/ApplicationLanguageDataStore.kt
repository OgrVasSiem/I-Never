package com.game.INever.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Locale

/*
class ApplicationLanguageDataStore(private val preferencesDataStore: DataStore<Preferences>) :
    DataStore<String> {
    companion object {
        val languageKey = stringPreferencesKey("language")
    }

    private val mutex = Mutex()

    override val data: Flow<String>
        get() = preferencesDataStore.data.map {
            it[languageKey] ?: Locale.getDefault().language
        }

    override suspend fun updateData(transform: suspend (t: String) -> String): String {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val updateData = preferencesDataStore.updateData {
                        preferencesOf(
                            languageKey to transform(it[languageKey] ?: Locale.getDefault().language)
                        )
                    }

                    updateData[languageKey] ?: Locale.getDefault().language
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                    return@withContext data.first()
                }
            }
        }
    }
}*/
