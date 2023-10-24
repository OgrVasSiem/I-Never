package com.game.INever.ui.destination.settings.Language
/*

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.INever.core.language.LanguageManager
import com.game.INever.dataStore.ApplicationLanguageDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationLanguageViewModel @Inject constructor(
    private val languageManager: LanguageManager,
    private val applicationLanguageDataStore: ApplicationLanguageDataStore
) : ViewModel() {
    fun updateApplicationLanguage(language: String, activity: Activity, context: Context) {
        viewModelScope.launch {
            languageManager.onLanguageSelected(
                context = context,
                activity = activity,
                language = language
            )
        }
    }

    var activeLanguage by mutableStateOf("en")
        private set

    init {
        viewModelScope.launch {
            applicationLanguageDataStore.data.collectLatest {
                activeLanguage = it
            }
        }
    }

    val languagesList = listOf("ru", "en")
}*/
