package com.foresko.game.dataStore

/*
class SystemRegionDataStore(private val preferencesDataStore: DataStore<Preferences>) :
    DataStore<String> {
    companion object {
        val systemRegionKey = stringPreferencesKey("systemRegionKey")
    }

    private val mutex = Mutex()

    override val data: Flow<String>
        get() = preferencesDataStore.data.map {
            it[systemRegionKey] ?: Locale.getDefault().language
        }

    override suspend fun updateData(transform: suspend (t: String) -> String): String {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val updateData = preferencesDataStore.updateData {
                        preferencesOf(
                            systemRegionKey to transform(
                                it[systemRegionKey] ?: Locale.getDefault().language
                            )
                        )
                    }

                    updateData[systemRegionKey] ?: Locale.getDefault().language
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                    return@withContext data.first()
                }
            }
        }
    }
}*/
