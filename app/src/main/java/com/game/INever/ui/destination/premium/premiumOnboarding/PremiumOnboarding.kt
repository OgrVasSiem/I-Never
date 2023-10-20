package com.game.INever.ui.destination.premium.premiumOnboarding

import android.app.Activity
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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.billingclient.api.ProductDetails
import com.game.INever.R
import com.game.INever.core.premium.TariffType
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.components.TopAppBar
import com.game.INever.ui.components.TopAppBarDefaults
import com.game.INever.ui.destination.premium.PremiumViewModel
import com.game.INever.ui.destination.premium.components.TariffDescription
import com.game.INever.ui.destination.premium.components.Tariffs
import com.game.INever.ui.destination.premium.premiumOnboarding.components.PremiumInfo
import com.game.INever.ui.destinations.destinations.MainScreenDestination
import com.game.INever.ui.destinations.destinations.OnboardingScreenDestination
import com.game.INever.ui.theme.INeverTheme
import com.game.INever.utils.LocalActivity
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
        if (viewModel.premiumIsActive == true || viewModel.onboardingState?.premium == false) {
            rootNavigator.navigate(MainScreenDestination()) {
                popUpTo(route = OnboardingScreenDestination.route) { inclusive = true }
            }
        }

        delay(300)

        if (viewModel.premiumIsActive != null && viewModel.onboardingState != null) initializeScreen =
            true
    }

    if (initializeScreen) {
        PremiumOnboardingScreen(
            viewModel = viewModel,
            navigateToChooseZodiacSign = {
                rootNavigator.navigate(
                    MainScreenDestination(

                    )
                )
            },
        )
    }
}

@Composable
private fun PremiumOnboardingScreen(
    viewModel: PremiumViewModel,
    navigateToChooseZodiacSign: () -> Unit,

) {
    val smallScreen = LocalConfiguration.current.screenWidthDp.dp < 375.dp

    val subscribeForSale by viewModel.subscribeForSaleFlows.collectAsState(initial = null)

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
            ) {
                PremiumInfo()

                if (!smallScreen) Spacer(modifier = Modifier.height(32.dp))

                Tariffs(
                    tariffType = viewModel.tariffType,
                    changeTariffType = viewModel::changeTariffType,
                    subscribeForSale = subscribeForSale
                )

                Spacer(modifier = Modifier.height(20.dp))

                PremiumButton(
                    tariffType = viewModel.tariffType,
                    buy = viewModel::buy,
                    subscribeForSale = subscribeForSale
                )

                Spacer(modifier = Modifier.height(16.dp))

                TariffDescription(
                    tariffType = viewModel.tariffType,
                    subscribeForSale = subscribeForSale,
                    isPremiumScreen = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                Info()
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
                    .padding(start = TopAppBarDefaults.DefaultButtonHorizontalPaddingValues)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = INeverTheme.colors.white
                )
            }
        }
    )
}

@Composable
private fun PremiumButton(
    tariffType: TariffType,
    buy: (productDetails: ProductDetails, activity: Activity, tag: String) -> Unit,
    subscribeForSale: ProductDetails?
) {
    val activity = LocalActivity.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 36.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(INeverTheme.colors.accent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                try {
                    subscribeForSale?.let { productDetails ->
                        buy(
                            productDetails,
                            activity,
                            tariffType.googlePurchaseName
                        )
                    }
                } catch (ex: Exception) {
                    FirebaseCrashlytics
                        .getInstance()
                        .recordException(ex)
                }
            }
    ) {
        Text(
            text = stringResource(R.string.try_free),
            color = INeverTheme.colors.primary,
            fontSize = 17.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight(600)
        )
    }
}

@Composable
private fun Info() {
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
        color = INeverTheme.colors.white.copy(alpha = 0.50f),
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