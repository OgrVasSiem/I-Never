package com.game.INever.core.utils

import android.app.Activity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalActivity = staticCompositionLocalOf<Activity> { error("") }

@OptIn(ExperimentalMaterialApi::class)
val LocalModalBottomSheetState = staticCompositionLocalOf<ModalBottomSheetState> { error("") }

val LocalSystemRegion = staticCompositionLocalOf<String> { error("") }