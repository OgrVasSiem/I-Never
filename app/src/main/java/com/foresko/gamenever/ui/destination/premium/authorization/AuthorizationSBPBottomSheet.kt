package com.foresko.gamenever.ui.destination.premium.authorization

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.LocaleManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.destination.premium.TariffType
import com.foresko.gamenever.core.utils.LocalModalBottomSheetState
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.RootNavigator
import com.foresko.gamenever.ui.destinations.destinations.PendingStatusPurchaseScreenDestination
import com.foresko.gamenever.ui.launchSingleTopNavigate
import com.foresko.gamenever.ui.theme.INeverTheme
import com.foresko.gamenever.ui.utils.formaters.currencyFormatter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(
    navArgsDelegate = AuthorizationSBPNavArgs::class,
    style = DestinationStyleBottomSheet::class
)
@RootNavGraph
fun AuthorizationSBPBottomSheet(
    viewModel: AuthorizationSBPViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = LocalModalBottomSheetState.current

    BackHandler { coroutineScope.launch { sheetState.hide() } }

    AuthorizationSBPBottomSheet(
        viewModel = viewModel,
        navigateToPendingStatusPurchaseScreen = {
            rootNavigator.launchSingleTopNavigate(
                PendingStatusPurchaseScreenDestination(
                    subscriptionId = viewModel.subscriptionId
                )
            )
        },
        popBackStack = {
            coroutineScope.launch { sheetState.hide() }
        }
    )
}

@Composable
private fun AuthorizationSBPBottomSheet(
    viewModel: AuthorizationSBPViewModel,
    navigateToPendingStatusPurchaseScreen: () -> Unit,
    popBackStack: () -> Unit
) {
    val systemRegion = LocaleManagerCompat.getSystemLocales(LocalContext.current).get(0)!!.toLanguageTag()

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = viewModel.authResult) { task ->
            viewModel.authorization(task = task)
        }

    NavigateLaunch(
        authorizationSBPState = viewModel.authorizationSBPState,
        navigateToPendingStatusPurchaseScreen = navigateToPendingStatusPurchaseScreen,
        popBackStack = popBackStack
    )

    ErrorToastLaunch(
        isNetworkConnectionError = viewModel.isNetworkConnectionError,
        changeNetworkConnectionErrorState = viewModel::changeNetworkConnectionErrorState
    )

    val text = remember(viewModel.inAppSubscription) {
        if (viewModel.inAppSubscription?.subscriptionName == TariffType.ThreeMonth.SBPSubscribeName) {
            R.string.premium_on_three_months
        } else {
            R.string.premium_on_one_year
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .background(INeverTheme.colors.white)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(text),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.body
        )

        Text(
            text = currencyFormatter(
                localeFormat = systemRegion,
                currencyCode = viewModel.inAppSubscription?.currencyCode
                    ?: Currency.getInstance(Locale.US).currencyCode
            ).format(viewModel.inAppSubscription?.price ?: BigDecimal(0)),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.bodySemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.description_for_sbp_autorization),
            color = INeverTheme.colors.primary,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight(400),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        AuthorizationButton(
            openGoogleAlertDialog = { authResultLauncher.launch(signInRequestCode) }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun AuthorizationButton(
    openGoogleAlertDialog: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(INeverTheme.colors.accent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true, color = INeverTheme.colors.white)
            ) {
                openGoogleAlertDialog()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = stringResource(R.string.continue_with_google),
                color = INeverTheme.colors.white,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600)
            )
        }
    }
}

@Composable
private fun ErrorToastLaunch(
    isNetworkConnectionError: Boolean,
    changeNetworkConnectionErrorState: (Boolean) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = isNetworkConnectionError) {
        if (isNetworkConnectionError) {
            Toast.makeText(
                context,
                context.getString(R.string.network_connection_error),
                Toast.LENGTH_SHORT
            ).show()

            delay(1000)

            changeNetworkConnectionErrorState(false)
        }
    }
}

@Composable
private fun NavigateLaunch(
    authorizationSBPState: AuthorizationSBPState,
    navigateToPendingStatusPurchaseScreen: () -> Unit,
    popBackStack: () -> Unit
) {
    LaunchedEffect(key1 = authorizationSBPState) {
        when (authorizationSBPState) {
            AuthorizationSBPState.WITH_PREMIUM -> popBackStack()
            AuthorizationSBPState.WITHOUT_PREMIUM -> navigateToPendingStatusPurchaseScreen()
            else -> {}
        }
    }
}

private const val signInRequestCode = 10001