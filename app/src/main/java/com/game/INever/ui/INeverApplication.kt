package com.game.INever.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.game.INever.ui.destinations.NavGraphs
import com.game.INever.ui.theme.INeverTheme
import com.game.INever.ui.utils.rememberBottomSheetNavigator
import com.game.INever.utils.LocalModalBottomSheetState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun INeverApplication() {
    val navController = rememberNavController()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = SwipeableDefaults.AnimationSpec,
        skipHalfExpanded = true
    )

    val bottomSheetNavigator = rememberBottomSheetNavigator(sheetState = sheetState)
    navController.navigatorProvider += bottomSheetNavigator

    INeverTheme {
        CompositionLocalProvider(LocalModalBottomSheetState provides sheetState) {
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            ) {
                DestinationsNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(INeverTheme.colors.bg),
                    navGraph = NavGraphs.root,
                    engine = rememberAnimatedNavHostEngine(
                        rootDefaultAnimations = RootNavGraphDefaultAnimations(
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None },
                            popEnterTransition = { EnterTransition.None },
                            popExitTransition = { ExitTransition.None }
                        ),
                        defaultAnimationsForNestedNavGraph = mapOf()
                    ),
                    navController = navController,
                    dependenciesContainerBuilder = {
                        dependency(RootNavigator(destinationsNavigator))
                    }
                )
            }
        }
    }
}