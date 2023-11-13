package com.foresko.gamenever.dataStore.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

abstract class ApplicationDataStore<T>(
    @ApplicationContext private val context: Context,
    private val fileName: String,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
) : DataStore<T> {

    private val preferencesDataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(fileName) }
    )

    private val mutex = Mutex()

    override val data: Flow<T>
        get() = preferencesDataStore.data.map {
            it[key] ?: defaultValue
        }

    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val updateData = preferencesDataStore.updateData { preferences ->
                        preferencesOf(
                            key to transform(preferences[key] ?: defaultValue)
                        )
                    }

                    updateData[key] ?: defaultValue
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                    return@withContext data.firstOrNull() ?: defaultValue
                }
            }
        }
    }
}