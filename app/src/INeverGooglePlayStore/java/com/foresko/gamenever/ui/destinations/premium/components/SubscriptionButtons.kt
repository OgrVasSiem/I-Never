package com.foresko.gamenever.ui.destinations.premium.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amplitude.api.Amplitude
import com.android.billingclient.api.ProductDetails
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.destination.premium.TariffType
import com.foresko.gamenever.core.utils.LocalActivity
import com.foresko.gamenever.dataStore.Session
import com.foresko.gamenever.ui.theme.INeverTheme
import com.foresko.gamenever.ui.utils.horizontalGradient
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.Locale

@Composable
fun SubscriptionButtons(
    tariffType: TariffType,
    subscribeForSale: ProductDetails?,
    subscribe: (productDetails: ProductDetails, WeakReference<Activity>, tag: String) -> Unit,
    navigateToAuthorizationSBPBottomSheet: () -> Unit,
    session: Session?,
    navigateToPendingStatusPurchaseScreen: () -> Unit
) {
    val activity = LocalActivity.current

    val isRussia = remember { Locale.getDefault().language == "ru" }

    Column(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
    ) {
        GooglePlaySubscription(
            isRussia = isRussia,
            subscribe = {
                try {
                    subscribeForSale?.let { productDetails ->
                        subscribe(
                            productDetails,
                            WeakReference(activity),
                            tariffType.googlePurchaseName
                        )
                    }
                } catch (ex: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(ex)
                }
            }
        )

        if (isRussia && tariffType != TariffType.Month) {
            Spacer(modifier = Modifier.height(14.dp))

            SBPSubscription(
                navigateToAuthorizationSBPBottomSheet = navigateToAuthorizationSBPBottomSheet,
                navigateToPendingStatusPurchaseScreen = navigateToPendingStatusPurchaseScreen,
                session = session
            )
        }
    }
}

@Composable
private fun GooglePlaySubscription(
    isRussia: Boolean,
    subscribe: () -> Unit
) {
    val text = remember(isRussia) {
        if (isRussia) R.string.subscription_with_google_play
        else R.string.subscribe
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        0f to Color(0xFF00D6BD),
                        0.469f to Color(0xFF00DAAB),
                        1f to Color(0xFF35F477)
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                subscribe()

                Amplitude.getInstance().logEvent(
                    "subscription_button",
                    JSONObject().put("path", "onboarding_welcome_screen")
                )
            }
    ) {
        Text(
            text = stringResource(text),
            color = INeverTheme.colors.white,
            style = INeverTheme.textStyles.bodySemiBold
        )
    }
}

@Composable
private fun SBPSubscription(
    navigateToAuthorizationSBPBottomSheet: () -> Unit,
    session: Session?,
    navigateToPendingStatusPurchaseScreen: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(100.dp))
            .border(1.dp, INeverTheme.colors.accent, RoundedCornerShape(100.dp))
            .background(Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                if (session == null) {
                    navigateToAuthorizationSBPBottomSheet()
                } else {
                    navigateToPendingStatusPurchaseScreen()
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.payment_through_SBP),
                color = INeverTheme.colors.primary,
                style = INeverTheme.textStyles.bodySemiBold
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                painter = painterResource(R.drawable.ic_sbp_logo),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}