package com.foresko.gamenever.ui.destination.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetPremiumQuery
import com.foresko.gamenever.application.operations.queries.dataStoreQueries.GetSessionQuery
import com.foresko.gamenever.dataStore.Session

import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val queryDispatcher: QueryDispatcher,
) : ViewModel() {
    var session by mutableStateOf<Session?>(null)
        private set

    var premiumIsActive by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            queryDispatcher.dispatch(GetPremiumQuery).collectLatest {
                premiumIsActive = it.isActive
            }
        }

        viewModelScope.launch {
            queryDispatcher.dispatch(GetPremiumQuery).collectLatest {
                premiumIsActive = it.isActive
            }
        }

        viewModelScope.launch {
            queryDispatcher.dispatch(GetSessionQuery).collectLatest {
                session = it
            }
        }
    }
}