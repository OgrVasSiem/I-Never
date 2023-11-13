package com.foresko.gamenever.dataStore

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import com.foresko.gamenever.dataStore.core.ApplicationDataStore
import dagger.hilt.android.qualifiers.ApplicationContext

class ReminderAboutPremiumDataStore(
    @ApplicationContext context: Context
) : ApplicationDataStore<Long>(
    context = context,
    fileName = "reminder_about_premium",
    key = longPreferencesKey("key_reminder_about_premium"),
    defaultValue = 1L
)