package com.foresko.gamenever.core.versionControl

import arrow.core.Either
import com.foresko.gamenever.application.core.query.QueryDispatcher
import com.foresko.gamenever.application.operations.queries.subscriptionQueries.GetAppUpdateAvailabilityStatusQuery
import com.foresko.gamenever.core.network.NetworkStatusTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class VersionService @Inject constructor(
    private val queryDispatcher: QueryDispatcher,
    private val networkStatusTracker: NetworkStatusTracker
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _versionStatus = MutableStateFlow<VersionStatusType?>(null)
    val versionStatus = _versionStatus.asStateFlow()

    init {
        coroutineScope.launch {
            getMinimumVersionCode()
        }
    }

    private suspend fun getMinimumVersionCode() {
        networkStatusTracker.networkStatus.collectLatest {
            queryDispatcher.dispatch(GetAppUpdateAvailabilityStatusQuery)
                .collectLatest { result ->
                    when (result) {
                        is Either.Left -> {
                            _versionStatus.value = VersionStatusType.UPDATE_NOT_AVAILABLE
                        }

                        is Either.Right -> {
                            _versionStatus.value = result.value
                        }
                    }
                }
        }

    }
}