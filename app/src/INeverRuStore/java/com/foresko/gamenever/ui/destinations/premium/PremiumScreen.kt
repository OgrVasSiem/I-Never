package com.foresko.gamenever.ui.premium

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.amplitude.api.Amplitude
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.RootNavigator
import com.foresko.gamenever.ui.destinations.destinations.PrivacyPolicyScreenDestination
import com.foresko.gamenever.ui.components.snackBar.NetworkConnectionErrorSnackBar
import com.foresko.gamenever.ui.destination.premium.components.PremiumPrivilege
import com.foresko.gamenever.ui.destinations.destinations.AuthorizationSBPBottomSheetDestination
import com.foresko.gamenever.ui.destinations.destinations.PendingStatusPurchaseScreenDestination
import com.foresko.gamenever.ui.destinations.destinations.TermOfUseScreenDestination
import com.foresko.gamenever.ui.destinations.premium.PremiumViewModel
import com.foresko.gamenever.ui.premium.components.SubscriptionButtons
import com.foresko.gamenever.ui.premium.components.Tariffs
import com.foresko.gamenever.ui.theme.INeverTheme
import com.foresko.gamenever.ui.utils.formaters.dateFormatter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId

@Composable
@Destination
@RootNavGraph
fun PremiumScreen(
    viewModel: PremiumViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    PremiumScreen(
        viewModel = viewModel,
        popBackStack = { rootNavigator.popBackStack() },
        navigateToPrivacyPolicyScreen = { rootNavigator.navigate(PrivacyPolicyScreenDestination) },

        navigateToTermOfUseScreen = { rootNavigator.navigate(TermOfUseScreenDestination) },

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

@Composable
private fun PremiumScreen(
    viewModel: PremiumViewModel,
    popBackStack: () -> Unit,
    navigateToPrivacyPolicyScreen: () -> Unit,
    navigateToTermOfUseScreen: () -> Unit,
    navigateToAuthorizationSBPBottomSheet: () -> Unit,
    navigateToPendingStatusPurchaseScreen: () -> Unit
) {
    Box {
        CloseButton(
            popBackStack = popBackStack,
            modifier = Modifier
                .align(Alignment.TopStart)
        )

        ErrorSnackBars(
            isNetworkConnectionError = viewModel.isNetworkConnectionError,
            changeNetworkConnectionErrorState = viewModel::changeNetworkConnectionErrorState,
            modifier = Modifier
                .zIndex(3f)
        )

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .background(INeverTheme.colors.white)
                .fillMaxSize()
                .verticalScroll(rememberScrollState(0))
                .statusBarsPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .zIndex(2f)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(54.dp))

                PremiumHeader(premiumIsActive = viewModel.premiumIsActive ?: false)

                Spacer(modifier = Modifier.height(24.dp))

                if (!viewModel.premiumIsActive) {
                    Tariffs(
                        inAppSubscriptions = viewModel.inAppSubscriptions,
                        tariffType = viewModel.tariffType,
                        changeTariffType = viewModel::changeTariffType
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Amplitude.getInstance().logEvent("subscription_button", JSONObject().put("path", "premium_screen"))

                    SubscriptionButtons(
                        navigateToPendingStatusPurchaseScreen = navigateToPendingStatusPurchaseScreen,
                        navigateToAuthorizationSBPBottomSheet = navigateToAuthorizationSBPBottomSheet,
                        session = viewModel.session
                    )
                } else {
                    InfoAboutPremium(endDateInMilli = viewModel.premiumEndDateInEpochMilli)
                }
                Spacer(modifier = Modifier.height(24.dp))

                PremiumPrivilege(premiumIsActive = viewModel.premiumIsActive ?: false)

                Spacer(modifier = Modifier.height(32.dp))

                Contacts()

                Spacer(modifier = Modifier.height(32.dp))

                SubscriptionInfo(
                    navigateToPrivacyPolicyScreen = navigateToPrivacyPolicyScreen,
                    navigateToTermOfUseScreen = navigateToTermOfUseScreen
                )

                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Composable
private fun CloseButton(
    popBackStack: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = popBackStack,
        modifier = modifier
            .zIndex(1f)
            .statusBarsPadding()
            .padding(start = 10.dp, top = 10.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = null,
        )
    }
}

@Composable
private fun PremiumHeader(premiumIsActive: Boolean) {
    val text = remember(premiumIsActive) {
        if (premiumIsActive) R.string.you_have_premium_version else R.string.get_premium_eng
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(7.dp))

        Icon(
            painter = painterResource(R.drawable.img_premium),
            contentDescription = null,
            modifier = Modifier.size(130.dp),
            tint = Color.Unspecified,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(text),
            color = INeverTheme.colors.primary,
            fontWeight = FontWeight(600),
            fontSize = 29.sp,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (premiumIsActive) {
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.thank_you_for_support),
                color = INeverTheme.colors.primary,
                fontWeight = FontWeight(400),
                fontSize = 15.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Contacts() {
    val context = LocalContext.current

    val intentTelegram = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(NonPremiumTelegramAddress))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.have_quetions_question),
            color = INeverTheme.colors.primary,
            fontSize = 22.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight(600)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.write_we_will_be_happy_to_answer),
            color = INeverTheme.colors.primary,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight(600)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFFB12A5B),
                            Color(0xFFCF556C),
                            Color(0xFFFF8C7F),
                            Color(0xFFFF8177)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true,
                        color = INeverTheme.colors.accent
                    )
                ) {
                    context.startActivity(intentTelegram)
                }
        ) {
            Text(
                text = stringResource(R.string.support),
                color = INeverTheme.colors.primary,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}



@Composable
private fun InfoAboutPremium(
    endDateInMilli: Long
) {
    Column {
        if (endDateInMilli != 0L) {
            Text(
                text = stringResource(
                    R.string.info_about_not_renewed_premium,
                    dateFormatter.format(
                        Instant.ofEpochMilli(endDateInMilli).atZone(ZoneId.systemDefault())
                    )
                ),
                color = INeverTheme.colors.primary.copy(alpha = 0.70f),
                fontWeight = FontWeight(400),
                fontSize = 15.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SubscriptionInfo(
    navigateToTermOfUseScreen: () -> Unit,
    navigateToPrivacyPolicyScreen: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFB12A5B),
                        Color(0xFFCF556C),
                        Color(0xFFFF8C7F),
                        Color(0xFFFF8177)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.term_of_use),
                color = INeverTheme.colors.primary,
                fontSize = 14.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight(400),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        navigateToTermOfUseScreen()
                    }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(R.string.privacy_policy),
                color = INeverTheme.colors.primary,
                fontSize = 14.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight(400),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        navigateToPrivacyPolicyScreen()
                    }
            )

            Spacer(modifier = Modifier.height(13.dp))

            Text(
                text = stringResource(R.string.subscribe_description),
                color = INeverTheme.colors.primary.copy(alpha = 0.52f),
                fontSize = 14.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight(400),
            )
        }
    }
}

@Composable
private fun ErrorSnackBars(
    isNetworkConnectionError: Boolean,
    changeNetworkConnectionErrorState: (Boolean) -> Unit,
    modifier: Modifier
) {
    NetworkConnectionErrorSnackBar(
        isNetworkConnectionError = isNetworkConnectionError,
        changeNetworkConnectionState = { changeNetworkConnectionErrorState(false) },
        modifier = modifier
            .padding(top = 48.dp)
    )
}

private const val NonPremiumTelegramAddress = "https://t.me/Foresko_Support"