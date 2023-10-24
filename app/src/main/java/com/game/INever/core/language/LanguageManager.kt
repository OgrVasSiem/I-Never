package com.game.INever.core.language


import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.util.Locale

/*
class LanguageManager(
    private val applicationLanguageDataStore: ApplicationLanguageDataStore,
    private val systemRegionDataStore: SystemRegionDataStore,
) {
    fun onLanguageSelected(context: Context, activity: Activity, language: String) {
        setLocale(context = context, language = language)

        restartActivity(activity = activity)
    }

    private fun restartActivity(activity: Activity) {
        val intent = Intent(activity, activity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun setLocale(context: Context, language: String? = null): String {
        var systemRegion: String = Locale.getDefault().language

        val locale = runBlocking {
            systemRegion = systemRegionDataStore.updateData { Locale.getDefault().language }

            if (language != null) {
                applicationLanguageDataStore.updateData { language }
            }

            applicationLanguageDataStore.data.firstOrNull()
                ?.let { Locale(it) } ?: Locale.getDefault()
        }

        Locale.setDefault(locale)

        val config = android.content.res.Configuration()

        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        return systemRegion
    }
}*/
