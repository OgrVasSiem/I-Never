package com.game.INever.ui.destination.premium.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.game.INever.R
import com.game.INever.core.premium.TariffType
import com.game.INever.core.premium.getSubscriptionInfo
import com.game.INever.ui.theme.INeverTheme
import com.game.INever.ui.utils.formaters.currencyFormatter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.math.BigDecimal

@Composable
fun TariffDescription(
    tariffType: TariffType,
    subscribeForSale: ProductDetails?,
    isPremiumScreen: Boolean = false
) {
    val density = LocalDensity.current
    var heightDescriptionText by remember { mutableStateOf(0.dp) }

    val text = remember(tariffType) {
        when (tariffType) {
            TariffType.Year -> R.string.trialPeriodYear
            TariffType.Month -> R.string.trialPeriodMonth
            TariffType.ThreeMonth -> R.string.trial_period_three_months
        }
    }

    var price by remember { mutableStateOf(BigDecimal(10)) }
    var currencyCode by remember { mutableStateOf("USD") }

    LaunchedEffect(key1 = subscribeForSale, tariffType) {
        try {
            val defaultSubscriptionInfo =
                getSubscriptionInfo(subscribeForSale, tariffType.googlePurchaseName)

            currencyCode = defaultSubscriptionInfo.currencyCode
            price = defaultSubscriptionInfo.price
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }

    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = heightDescriptionText)
    ) {
        Text(
            text = stringResource(text, currencyFormatter(currencyCode).format(price)),
            color = if (!isPremiumScreen) INeverTheme.colors.primary.copy(alpha = 0.70f)
            else INeverTheme.colors.primary,
            fontSize = 13.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight(400),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .onGloballyPositioned {
                    heightDescriptionText = with(density) { it.size.height.toDp() }
                }
        )
    }
}