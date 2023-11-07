package com.foresko.game.ui.destination.main.components

import android.app.Activity
import android.content.ContextWrapper
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.foresko.game.R
import com.foresko.game.core.rest.Card
import com.foresko.game.ui.destination.game.CloseButton
import com.foresko.game.ui.theme.INeverTheme


@Composable
fun TestGameDialog(
    card: Card,
    showDialog: MutableState<Boolean>,
    showAds: () -> Unit
) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(382.dp, 434.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.White)
                ) {
                    CloseButton(onDismissRequest = { showDialog.value = false })

                    PremDialog(
                        card.alertImage,
                        showAds = showAds
                    )
                }
            }
        }
    }
}

@Composable
fun PremDialog(
    iconUri: String,
    showAds: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(44.dp))

        Image(
            painter = rememberAsyncImagePainter(Uri.parse(iconUri)),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.close_category),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.boldButtonText,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = stringResource(id = R.string.get_free),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.body,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        PremiumButton(
            showAds = showAds
        )
    }
}

@Composable
private fun PremiumButton(
    showAds: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(INeverTheme.colors.accent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                 showAds()
            }
    ) {
        Text(
            text = stringResource(R.string.watch_advertising),
            color = INeverTheme.colors.white,
            fontSize = 17.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(horizontal = 36.dp)
        )
    }
}