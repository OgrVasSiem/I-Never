package com.foresko.gamenever.dataStore

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
