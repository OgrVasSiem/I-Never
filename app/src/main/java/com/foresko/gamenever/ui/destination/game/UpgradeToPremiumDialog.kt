package com.foresko.gamenever.ui.destination.game

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.theme.INeverTheme

@Composable
fun UpgradeToPremiumDialog(
    showDialog: MutableState<Boolean>,
    navigateToTermOfUse: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit
) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                ) {
                    CloseButton(onDismissRequest = { showDialog.value = false })

                    FinalDialog(
                        navigateToTermOfUse = navigateToTermOfUse,
                        navigateToPrivacyPolicy = navigateToPrivacyPolicy
                    )
                }
            }
        }
    }
}

@Composable
private fun FinalDialog(
    navigateToTermOfUse: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.img_cherry),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.premium_alert_title),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.text2,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(62.dp)
        )
        Text(
            text = stringResource(id = R.string.premium_alert_subtitle),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.boldText
        )

        Spacer(modifier = Modifier.height(30.dp))

        PremiumButton()

        Spacer(modifier = Modifier.height(46.dp))

        Info(
            navigateToTermOfUse = navigateToTermOfUse,
            navigateToPrivacyPolicy = navigateToPrivacyPolicy
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
@Composable
fun CloseButton(onDismissRequest: () -> Unit) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onDismissRequest() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
}

@Composable
private fun Info(
    navigateToTermOfUse: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit
) {
    Row( modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        InfoItem(
            text = R.string.term_of_use,
            onClick = navigateToTermOfUse
        )

        Spacer(modifier = Modifier.width(60.dp))

        InfoItem(
            text = R.string.privacy_policy,
            onClick = navigateToPrivacyPolicy
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
        color = INeverTheme.colors.primary.copy(alpha = 0.30f),
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight(400),
        textDecoration = TextDecoration.Underline,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
            .width(IntrinsicSize.Min)
    )
}

@Composable
private fun PremiumButton() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(INeverTheme.colors.accent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
            }
    ) {
        Text(
            text = stringResource(R.string.try_free),
            color = INeverTheme.colors.white,
            fontSize = 17.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(vertical = 19.dp)
        )
    }
}