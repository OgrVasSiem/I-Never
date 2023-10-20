package com.game.INever.ui.destination.premium

import android.app.Activity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.billingclient.api.ProductDetails
import com.game.INever.R
import com.game.INever.core.premium.TariffType
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.destination.premium.components.TariffDescription
import com.game.INever.ui.destination.premium.components.Tariffs
import com.game.INever.ui.theme.INeverTheme
import com.game.INever.ui.utils.linearGradient
import com.game.INever.utils.LocalActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
@RootNavGraph
fun PremiumScreen(
    viewModel: PremiumViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    PremiumScreen(
        viewModel = viewModel,
        popBackStack = { rootNavigator.popBackStack() },
    )
}

@Composable
private fun PremiumScreen(
    viewModel: PremiumViewModel,
    popBackStack: () -> Unit,
) {
    val subscribeForSale by viewModel.subscribeForSaleFlows.collectAsState(initial = null)

    Box {
        CloseButton(
            popBackStack = popBackStack,
            modifier = Modifier
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .background(brush = Brush.linearGradient(INeverTheme.gradients.gradientDark2))
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

                if (viewModel.premiumIsActive == false) {
                    Tariffs(
                        tariffType = viewModel.tariffType,
                        changeTariffType = viewModel::changeTariffType,
                        subscribeForSale = subscribeForSale,
                        isPremiumScreen = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TariffDescription(
                        tariffType = viewModel.tariffType,
                        subscribeForSale = subscribeForSale,
                        isPremiumScreen = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    SubscriptionButton(
                        tariffType = viewModel.tariffType,
                        buy = viewModel::buy,
                        subscribeForSale = subscribeForSale
                    )
                } else {
                    InfoAboutPremium()
                }

                Spacer(modifier = Modifier.height(32.dp))

                Contacts()

                Spacer(modifier = Modifier.height(32.dp))

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
            tint = INeverTheme.colors.white
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
            painter = painterResource(R.drawable.ic_keep),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(34.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(text),
            color = INeverTheme.colors.accent,
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
                color = INeverTheme.colors.white,
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
            color = INeverTheme.colors.accent,
            fontSize = 22.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight(600)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.write_we_will_be_happy_to_answer),
            color = INeverTheme.colors.accent,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight(600)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .border(1.dp, INeverTheme.colors.accent, RoundedCornerShape(100.dp))
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
                color = INeverTheme.colors.accent,
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
private fun SubscriptionButton(
    tariffType: TariffType,
    buy: (productDetails: ProductDetails, activity: Activity, tag: String) -> Unit,
    subscribeForSale: ProductDetails?
) {
    val activity = LocalActivity.current

    val accent2Gradient = Brush.linearGradient(INeverTheme.gradients.accent2)
    val accent2GradientWithAlpha = Brush.linearGradient(INeverTheme.gradients.accent2WithAlpha)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(brush = Brush.linearGradient(INeverTheme.gradients.accent2))
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
            text = stringResource(R.string.subscribe),
            color = INeverTheme.colors.primary,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(600)
        )
    }
}

@Composable
private fun InfoAboutPremium() {
    val context = LocalContext.current

    val intent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/account/subscriptions?hl=ru&gl=US")
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .defaultMinSize(minHeight = 60.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .border(1.dp, INeverTheme.colors.accent, RoundedCornerShape(100.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                context.startActivity(intent)
            }
    ) {
        Text(
            text = stringResource(R.string.manage_subscribe),
            color = INeverTheme.colors.white,
            style = INeverTheme.textStyles.bodySemiBold
        )
    }
}

private const val NonPremiumTelegramAddress = "https://t.me/Foresko_Support"