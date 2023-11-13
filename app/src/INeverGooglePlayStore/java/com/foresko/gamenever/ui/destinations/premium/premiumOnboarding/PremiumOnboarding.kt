package com.foresko.gamenever.ui.destinations.premium.premiumOnboarding

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.RootNavigator
import com.foresko.gamenever.ui.components.TopAppBar
import com.foresko.gamenever.ui.destination.premium.premiumOnboarding.components.PremiumInfo
import com.foresko.gamenever.ui.destinations.destinations.AuthorizationSBPBottomSheetDestination
import com.foresko.gamenever.ui.destinations.destinations.MainScreenDestination
import com.foresko.gamenever.ui.destinations.destinations.OnboardingScreenDestination
import com.foresko.gamenever.ui.destinations.destinations.PendingStatusPurchaseScreenDestination
import com.foresko.gamenever.ui.destinations.destinations.PrivacyPolicyScreenDestination
import com.foresko.gamenever.ui.destinations.destinations.TermOfUseScreenDestination
import com.foresko.gamenever.ui.destinations.premium.PremiumViewModel
import com.foresko.gamenever.ui.destinations.premium.components.SubscriptionButtons
import com.foresko.gamenever.ui.destinations.premium.components.Tariffs
import com.foresko.gamenever.ui.theme.INeverTheme
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay

@Composable
@Destination
@RootNavGraph
fun PremiumOnboardingScreen(
    viewModel: PremiumViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    var initializeScreen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.premiumIsActive, key2 = viewModel.onboardingState) {
        if (viewModel.premiumIsActive || viewModel.onboardingState?.premium == false) {
            rootNavigator.navigate(MainScreenDestination()) {
                popUpTo(route = OnboardingScreenDestination.route) { inclusive = true }
            }
        }

        delay(300)

        if (viewModel.onboardingState != null) initializeScreen =
            true
    }

    if (initializeScreen) {
        PremiumOnboardingScreen(
            viewModel = viewModel,
            navigateToChooseZodiacSign = {
                rootNavigator.navigate(
                    MainScreenDestination()
                )
            },
            navigateToPrivacyPolicy = { rootNavigator.navigate(PrivacyPolicyScreenDestination) },
            navigateToTermOfUse = { rootNavigator.navigate(TermOfUseScreenDestination) },
            navigateToAuthorizationSBPBottomSheet = {
                rootNavigator.navigate(
                    AuthorizationSBPBottomSheetDestination(subscriptionId = viewModel.subscriptionId)
                )
            },

            navigateToPendingStatusPurchaseScreen = {
                rootNavigator.navigate(
                    PendingStatusPurchaseScreenDestination(
                        subscriptionId = viewModel.subscriptionId
                    )
                )
            }
        )
    }
}

@Composable
private fun PremiumOnboardingScreen(
    viewModel: PremiumViewModel,
    navigateToChooseZodiacSign: () -> Unit,
    navigateToTermOfUse: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToAuthorizationSBPBottomSheet: () -> Unit,
    navigateToPendingStatusPurchaseScreen: () -> Unit
) {
    val productDetails by viewModel.productDetails.collectAsState(initial = null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(INeverTheme.colors.white),
    ) {
        Scaffold(
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .background(Color.Transparent)
                .navigationBarsPadding()
                .statusBarsPadding(),
            topBar = { TopAppBar(navigateToChooseZodiacSign = navigateToChooseZodiacSign) }
        ) { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(paddingValues)
                    .offset(y = (-20).dp)
            ) {
                PremiumInfo()

                Spacer(modifier = Modifier.height(10.dp))

                Tariffs(
                    tariffType = viewModel.tariffType,
                    changeTariffType = viewModel::changeTariffType,
                    subscribeForSale = productDetails
                )

                Spacer(modifier = Modifier.height(20.dp))

                SubscriptionButtons(
                    tariffType = viewModel.tariffType,
                    subscribe = viewModel::buyGoogleSubscription,
                    subscribeForSale = productDetails,
                    navigateToPendingStatusPurchaseScreen = navigateToPendingStatusPurchaseScreen,
                    navigateToAuthorizationSBPBottomSheet = navigateToAuthorizationSBPBottomSheet,
                    account = viewModel.account
                )

                Spacer(modifier = Modifier.height(16.dp))

                Info(
                    navigateToTermOfUse = navigateToTermOfUse,
                    navigateToPrivacyPolicy = navigateToPrivacyPolicy
                )
            }
        }
    }
}

@Composable
private fun TopAppBar(
    navigateToChooseZodiacSign: () -> Unit
) {
    TopAppBar(
        startItem = { modifier ->
            IconButton(
                onClick = navigateToChooseZodiacSign,
                modifier = modifier
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = INeverTheme.colors.primary
                )
            }
        }
    )
}

@Composable
private fun Info(
    navigateToTermOfUse: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit
) {
    val context = LocalContext.current

    val intent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/account/subscriptions?hl=ru&gl=US")
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        InfoItem(
            text = R.string.privacy_policy_eng,
            onClick = navigateToPrivacyPolicy
        )

        InfoItem(
            text = R.string.term_of_use_eng,
            onClick = navigateToTermOfUse
        )

        InfoItem(
            text = R.string.restore,
            onClick = {
                try {
                    context.startActivity(intent)
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                }
            }
        )
    }
}

@Composable
private fun InfoItem(
    @StringRes text: Int,
    onClick: () -> Unit
) {
    Text(
        text = stringResource(text),
        color = INeverTheme.colors.primary.copy(alpha = 0.50f),
        fontSize = 12.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight(400),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    )
}