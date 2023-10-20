package com.game.INever.ui.destination.onboarding.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.game.INever.R
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.destination.onboarding.OnboardingViewModel
import com.game.INever.ui.destinations.destinations.MainScreenDestination
import com.game.INever.ui.destinations.destinations.OnboardingScreenDestination
import com.game.INever.ui.destinations.destinations.PremiumOnboardingScreenDestination
import com.game.INever.ui.theme.INeverTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RootNavGraph(start = true)
@Destination
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    var initializeScreen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.onboardingState, key2 = viewModel.premiumIsActive) {
        if (viewModel.onboardingState?.default == false) {
            if (viewModel.premiumIsActive == true || viewModel.onboardingState?.premium == false) {
                rootNavigator.navigate(MainScreenDestination) {
                    popUpTo(route = OnboardingScreenDestination.route) { inclusive = true }
                }
            } else {
                rootNavigator.navigate(PremiumOnboardingScreenDestination) {
                    popUpTo(route = OnboardingScreenDestination.route) { inclusive = true }
                }
            }
        }

        delay(300)

        if (viewModel.onboardingState != null && viewModel.premiumIsActive != null) initializeScreen =
            true
    }

    if (initializeScreen) {
        OnboardingScreen(
            viewModel = viewModel,
            navigateToPremiumOnboardingScreen = {
                rootNavigator.navigate(PremiumOnboardingScreenDestination) {
                    popUpTo(route = OnboardingScreenDestination.route) { inclusive = true }
                }
            },
            navigateToMainScreen = {
                rootNavigator.navigate(MainScreenDestination) {
                    popUpTo(route = OnboardingScreenDestination.route) { inclusive = true }
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    navigateToPremiumOnboardingScreen: () -> Unit,
    navigateToMainScreen: () -> Unit
) {
    val pageCount = 3

    val pagerState = rememberPagerState(initialPage = 0) { pageCount }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(INeverTheme.colors.white),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) {
                when (it) {
                    0 -> Onboarding(
                        image = R.drawable.img_onboarding,
                        info = R.string.info_onboarding_first,
                        infoDescription = R.string.info_description_onboarding_first,
                        buttonName = R.string.great,
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }

                    1 -> Onboarding(
                        image = R.drawable.img_onboarding_2,
                        info = R.string.info_onboarding_second,
                        infoDescription = R.string.info_description_onboarding_second,
                        buttonName = R.string.good,
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }

                    2 -> Onboarding(
                        image = R.drawable.img_onboarding_3,
                        info = R.string.info_onboarding_thirth,
                        infoDescription = R.string.info_description_onboarding_thirth,
                        buttonName = R.string.continue_text,
                    ) {
                        navigateToPremiumOnboardingScreen()
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            StaticPagerIndicator(pagerState = pagerState)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaticPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFFD9D9D9),
    inactiveColor: Color = Color(0xFFD9D9D9).copy(alpha = 0.30f),
    indicatorWidth: Dp = 6.dp,
    indicatorHeight: Dp = 6.dp,
    spacing: Dp = 13.dp
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .padding(spacing / 2)
                    .size(indicatorWidth, indicatorHeight)
                    .background(
                        if (pagerState.currentPage == index) activeColor else inactiveColor,
                        CircleShape
                    )
            )
        }
    }
}