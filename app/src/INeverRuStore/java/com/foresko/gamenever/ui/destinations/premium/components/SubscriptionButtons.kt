package com.foresko.gamenever.ui.premium.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amplitude.api.Amplitude
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.foresko.gamenever.R
import com.foresko.gamenever.dataStore.Session
import com.foresko.gamenever.ui.theme.INeverTheme
import org.json.JSONObject

@Composable
fun SubscriptionButtons(
    navigateToAuthorizationSBPBottomSheet: () -> Unit,
    session: Session?,
    navigateToPendingStatusPurchaseScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
    ) {
        SBPSubscription(
            navigateToAuthorizationSBPBottomSheet = navigateToAuthorizationSBPBottomSheet,
            navigateToPendingStatusPurchaseScreen = navigateToPendingStatusPurchaseScreen,
            session = session
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