package com.foresko.gamenever.ui.destinations.premium.components

import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.LocaleManagerCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.getSpans
import com.android.billingclient.api.ProductDetails
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.destination.premium.TariffType
import com.foresko.gamenever.ui.destinations.premium.SubscriptionInfo
import com.foresko.gamenever.ui.theme.INeverTheme
import com.foresko.gamenever.ui.utils.formaters.formatAsMonthWordEnding
import com.foresko.gamenever.ui.utils.formaters.moneyCurrencyFormatter
import com.foresko.gamenever.ui.utils.formaters.numberFormatter
import com.foresko.gamenever.ui.utils.linearGradient
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

@Composable
fun Tariffs(
    tariffType: TariffType,
    changeTariffType: (TariffType) -> Unit,
    subscribeForSale: ProductDetails?,
    isPremiumScreen: Boolean = false
) {
    var monthSubscribePrice by remember { mutableStateOf(BigDecimal.ZERO) }

    var yearSubscribePrice by remember { mutableStateOf(BigDecimal(0)) }
    var monthPriceInYearlySubscribe by remember { mutableStateOf(BigDecimal(0)) }

    var threeMonthSubscribePrice by remember { mutableStateOf(BigDecimal(0)) }
    var monthPriceInThreeMonthlySubscribe by remember { mutableStateOf(BigDecimal(0)) }

    var currencyCode by remember { mutableStateOf("") }

    var percent by remember { mutableStateOf(BigDecimal(0)) }

    var trialIsActive by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = subscribeForSale) {
        try {
            val defaultSubscriptionInfo =
                SubscriptionInfo.from(subscribeForSale, TariffType.Year.googlePurchaseName)

            monthSubscribePrice =
                SubscriptionInfo.from(subscribeForSale, TariffType.Month.googlePurchaseName).price

            yearSubscribePrice = defaultSubscriptionInfo.price

            monthPriceInYearlySubscribe =
                (yearSubscribePrice.divide(BigDecimal(12), 2, RoundingMode.HALF_DOWN))

            threeMonthSubscribePrice =
                SubscriptionInfo.from(
                    subscribeForSale,
                    TariffType.ThreeMonth.googlePurchaseName
                ).price

            monthPriceInThreeMonthlySubscribe =
                (threeMonthSubscribePrice.divide(BigDecimal(3), 2, RoundingMode.HALF_DOWN))

            if (monthSubscribePrice != BigDecimal.ZERO) {
                percent = BigDecimal(100) - (monthPriceInYearlySubscribe * BigDecimal(100))
                    .divide(monthSubscribePrice, 0, RoundingMode.HALF_DOWN)
            }

            currencyCode = defaultSubscriptionInfo.currencyCode
            trialIsActive = defaultSubscriptionInfo.hasFreeTrial
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }


    val animatedScaleXMonth by animateFloatAsState(
        if (tariffType != TariffType.Month) 0.8f else 1f,
        label = ""
    )

    val animatedScaleXThreeMonth by animateFloatAsState(
        if (tariffType != TariffType.Year) 0.8f else 1f,
        label = ""
    )
    val animatedThreeMonthHeight by animateDpAsState(
        if (tariffType != TariffType.Year) 142.dp else 156.dp,
        label = ""
    )

    val animatedScaleXYear by animateFloatAsState(
        if (tariffType != TariffType.ThreeMonth) 0.8f else 1f,
        label = ""
    )

    val animatedPercentHorizontalPadding by animateDpAsState(
        if (tariffType == TariffType.Year) 14.dp else 4.dp,
        label = ""
    )


    val accent2Gradient = SolidColor(INeverTheme.colors.accent)
    val accentColor = SolidColor(INeverTheme.colors.accent)

    val percentColor = remember(tariffType) {
        if (tariffType == TariffType.ThreeMonth) accent2Gradient else accentColor
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(156.dp)
                .fillMaxWidth()
        ) {
            DefaultTariffBox(
                modifier = Modifier.weight(animatedScaleXMonth),
                isActive = tariffType == TariffType.Month,
                changeTariffType = { changeTariffType(TariffType.Month) },
                typeName = 1.formatAsMonthWordEnding().lowercase(),
                premiumPrice = monthSubscribePrice,
                currencyCode = currencyCode,
                monthPremiumPrice = null,
                typeTextCount = 1
            )

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .height(animatedThreeMonthHeight)
                    .weight(animatedScaleXThreeMonth)
            ) {
                if (percent != BigDecimal.ZERO) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .zIndex(1f)
                            .padding(horizontal = animatedPercentHorizontalPadding)
                            .defaultMinSize(minHeight = 24.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(19.dp))
                            .background(percentColor)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                val formattedText = HtmlCompat.fromHtml(
                                    stringResource(
                                        R.string.discount,
                                        numberFormatter(percent).format(percent)
                                    ),
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                )

                                append(formattedText.toString())

                                for (styleSpan in formattedText.getSpans<StyleSpan>()) {
                                    val spanStyle = when (styleSpan.style) {
                                        Typeface.BOLD -> SpanStyle(
                                            fontSize = 15.sp,
                                        )

                                        else -> SpanStyle()
                                    }

                                    addStyle(
                                        spanStyle,
                                        formattedText.getSpanStart(styleSpan),
                                        formattedText.getSpanEnd(styleSpan)
                                    )
                                }
                            },
                            color = if (!isPremiumScreen) INeverTheme.colors.white
                            else INeverTheme.colors.white,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight(600)
                        )
                    }
                }


                DefaultTariffBox(
                    isActive = tariffType == TariffType.Year,
                    changeTariffType = { changeTariffType(TariffType.Year) },
                    typeName = stringResource(R.string.year).lowercase(),
                    premiumPrice = yearSubscribePrice,
                    monthPremiumPrice = monthPriceInYearlySubscribe,
                    currencyCode = currencyCode,
                    modifier = Modifier
                        .align(Alignment.Center),
                    typeTextCount = 1
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            DefaultTariffBox(
                modifier = Modifier.weight(animatedScaleXYear),
                isActive = tariffType == TariffType.ThreeMonth,
                changeTariffType = { changeTariffType(TariffType.ThreeMonth) },
                typeName = 3.formatAsMonthWordEnding().lowercase(),
                premiumPrice = threeMonthSubscribePrice,
                currencyCode = currencyCode,
                monthPremiumPrice = monthPriceInThreeMonthlySubscribe,
                typeTextCount = 3
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DescriptionText(
            tariffType = tariffType,
            currencyCode = currencyCode,
            yearPrice = yearSubscribePrice,
            trialIsActive = trialIsActive
        )
    }
}

@Composable
private fun DefaultTariffBox(
    modifier: Modifier = Modifier,
    typeTextCount: Int,
    isActive: Boolean,
    changeTariffType: () -> Unit,
    typeName: String,
    premiumPrice: BigDecimal,
    currencyCode: String,
    monthPremiumPrice: BigDecimal? = null
) {
    val systemRegion =
        LocaleManagerCompat.getSystemLocales(LocalContext.current).get(0)!!.toLanguageTag()

    val accent2Gradient = Brush.linearGradient(INeverTheme.gradients.accent2)
    val accent2GradientWithAlpha = Brush.linearGradient(INeverTheme.gradients.accent2WithAlpha)

    val (borderBrush, borderWidth) = remember(isActive) {
        if (!isActive) accent2GradientWithAlpha to 2.dp
        else accent2Gradient to 3.dp
    }

    val textColor = remember(isActive) {
        if (!isActive) Color.Black.copy(0.30f) else Color.Black
    }

    val monthPriceTextColor = remember(isActive) {
        if (!isActive) Color.Black.copy(0.30f) else Color.Black.copy(0.70f)
    }

    val gradientBackground = INeverTheme.gradients.gradientBackround

    val gradientBackground2 = INeverTheme.gradients.gradientBackround2

    val monthPriceBackgroundColor = remember(isActive, gradientBackground) {
        if (isActive) Brush.linearGradient(gradientBackground)
        else Brush.linearGradient(gradientBackground2)
    }

    val animatedHeight by animateDpAsState(if (!isActive) 114.dp else 138.dp, label = "")

    val animatedValueTypeTextCount by animateFloatAsState(if (!isActive) 32f else 38f, label = "")

    val animatedFontSizeTypeName by animateFloatAsState(if (!isActive) 14f else 16f, label = "")

    val animatedValuePrice by animateFloatAsState(if (!isActive) 17f else 20f, label = "")

    val animatedValueMonthPrice by animateFloatAsState(if (!isActive) 13f else 14f, label = "")

    Box(
        modifier = modifier
            .height(animatedHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(monthPriceBackgroundColor)
            .border(
                width = borderWidth,
                brush = borderBrush,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                changeTariffType()
            }
    ) {
        if (premiumPrice == BigDecimal.ZERO) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center),
                    strokeWidth = 2.dp,
                    color = INeverTheme.colors.primary
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = typeTextCount.toString(),
                    color = textColor,
                    fontSize = animatedValueTypeTextCount.sp,
                    lineHeight = animatedValueTypeTextCount.sp,
                    fontWeight = FontWeight(600),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    )
                )

                Text(
                    text = typeName.lowercase(),
                    color = textColor,
                    fontSize = animatedFontSizeTypeName.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(400),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        )
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = moneyCurrencyFormatter(systemRegion, currencyCode, premiumPrice)
                        .format(premiumPrice),
                    color = textColor,
                    fontSize = animatedValuePrice.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(600),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (monthPremiumPrice != null) {
                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = stringResource(
                            R.string.price_in_month,
                            moneyCurrencyFormatter(systemRegion, currencyCode, premiumPrice)
                                .format(monthPremiumPrice),
                            stringResource(R.string.month_1).lowercase()
                        ),
                        color = monthPriceTextColor,
                        fontSize = animatedValueMonthPrice.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(400),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
private fun DescriptionText(
    tariffType: TariffType,
    currencyCode: String,
    yearPrice: BigDecimal,
    trialIsActive: Boolean
) {
    val systemRegion =
        LocaleManagerCompat.getSystemLocales(LocalContext.current).get(0)!!.toLanguageTag()

    val isRussia = remember {
        try {
            systemRegion.uppercase() == "RU"
        } catch (ex: Exception) {
            Locale.getDefault().country == "RU"
        }
    }

    val text = remember(tariffType, trialIsActive) {
        when (tariffType) {
            TariffType.Month -> {
                if (isRussia) R.string.premium_access_on_one_month
                else R.string.premium_access_on_month_long
            }

            TariffType.Year -> {
                if (trialIsActive) {
                    R.string.premium_access_on_one_year_with_trial_period
                } else {
                    if (isRussia) R.string.premium_access_on_one_year
                    else R.string.premium_access_on_one_year_long
                }
            }

            TariffType.ThreeMonth -> {
                if (isRussia) R.string.premium_access_on_three_months
                else R.string.premium_access_on_three_months_long
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        if (currencyCode.isNotEmpty()) {
            Text(
                text = stringResource(
                    text,
                    moneyCurrencyFormatter(systemRegion, currencyCode, yearPrice).format(yearPrice)
                ),
                color = INeverTheme.colors.primary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600),
                textAlign = TextAlign.Center
            )
        }
    }
}