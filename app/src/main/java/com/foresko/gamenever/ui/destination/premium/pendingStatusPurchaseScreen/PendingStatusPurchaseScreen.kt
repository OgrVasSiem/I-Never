package com.foresko.gamenever.ui.destination.premium.pendingStatusPurchaseScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.RootNavigator
import com.foresko.gamenever.ui.components.FloatingActionButton
import com.foresko.gamenever.ui.components.TopAppBar
import com.foresko.gamenever.ui.components.snackBar.NetworkConnectionErrorSnackBar
import com.foresko.gamenever.ui.components.snackBar.ServerErrorSnackBar
import com.foresko.gamenever.ui.theme.INeverTheme
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.foresko.gamenever.ui.utils.transactions.UpDownTransaction
@Composable
@Destination(
    navArgsDelegate = PendingStatusPurchaseNavArgs::class,
    style = UpDownTransaction::class
)
@RootNavGraph
fun PendingStatusPurchaseScreen(
    viewModel: PendingStatusPurchaseViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    PendingStatusPurchaseScreen(
        viewModel = viewModel,
        popBackStack = { rootNavigator.popBackStack() }
    )
}

@Composable
private fun PendingStatusPurchaseScreen(
    viewModel: PendingStatusPurchaseViewModel,
    popBackStack: () -> Unit
) {
    val context = LocalContext.current

    //navigate to SBP when get confirmationUrl
    LaunchedEffect(key1 = viewModel.confirmationUrl) {
        if (viewModel.confirmationUrl.isNotEmpty()) {
            val intentSBP = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(viewModel.confirmationUrl)
            )

            context.startActivity(intentSBP)
        }
    }

    LaunchedEffect(key1 = viewModel.purchaseStatus) {
        if (viewModel.purchaseStatus == PurchaseStatus.Confirmed) {
            popBackStack()
        } else if (viewModel.purchaseStatus == PurchaseStatus.Canceled) {
            popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        backgroundColor = INeverTheme.colors.white,
        topBar = {
            TopAppBar(popBackStack = popBackStack)
        }
    ) { paddingValues ->
        ErrorSnackBars(
            isNetworkConnectionError = viewModel.isNetworkConnectionError,
            changeNetworkConnectionErrorState = viewModel::changeNetworkConnectionErrorState,
            isServerError = viewModel.isServerError,
            changeServerErrorState = viewModel::changeServerErrorState,
            modifier = Modifier
                .zIndex(3f)
        )

        Box(
            contentAlignment = BiasAlignment(0f, -0.45f),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                CoinAnimation()

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.end_pay_in_bank_application),
                    color = INeverTheme.colors.primary,
                    style = INeverTheme.textStyles.body,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                FloatingActionButton(
                    background = INeverTheme.colors.white,
                    onClick = { popBackStack() },
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = INeverTheme.colors.accent,
                        style = INeverTheme.textStyles.bodySemiBold,
                        modifier = Modifier
                            .padding(horizontal = 38.dp, vertical = 18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(
    popBackStack: () -> Unit
) {
    TopAppBar(
        startItem = { modifier ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .padding(start = 18.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(INeverTheme.colors.white)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    ) {
                        popBackStack()
                    }
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
private fun CoinAnimation() {
    val coin = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.coin))

    Box(
        modifier = Modifier
            .size(220.dp)
    ) {
        if (coin.isSuccess) {
            val progress by animateLottieCompositionAsState(
                coin.value,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = coin.value,
                progress = progress,
                enableMergePaths = true,
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = stringResource(R.string.waiting_pay),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.body,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ErrorSnackBars(
    isNetworkConnectionError: Boolean,
    changeNetworkConnectionErrorState: (Boolean) -> Unit,
    isServerError: Boolean,
    changeServerErrorState: (Boolean) -> Unit,
    modifier: Modifier
) {
    NetworkConnectionErrorSnackBar(
        isNetworkConnectionError = isNetworkConnectionError,
        changeNetworkConnectionState = { changeNetworkConnectionErrorState(false) },
        modifier = modifier
            .padding(top = 48.dp)
    )

    ServerErrorSnackBar(
        isServerError = isServerError,
        changeServerErrorState = { changeServerErrorState(false) },
        modifier = modifier
            .padding(top = 48.dp)
    )
}