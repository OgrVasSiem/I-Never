package com.foresko.gamenever.ui.destination.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.theme.INeverTheme

@Composable
fun EndGameDialog(
    showDialog: MutableState<Boolean>,
) {
    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false }
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                ) {
                    EndGameDialogContent(onDismiss = { showDialog.value = false })
                }
            }
        }
    }
}

@Composable
fun EndGameDialogContent(
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Image(
            painter = painterResource(R.drawable.img_flag),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(id = R.string.game_over),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.gameOver,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        Text(
            text = stringResource(id = R.string.game_over_text),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.gameOverSubtitle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        PremiumButton(onClick = onDismiss)

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
private fun PremiumButton(
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(INeverTheme.colors.accent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
    ) {
        Text(
            text = stringResource(R.string.close),
            color = INeverTheme.colors.white,
            fontSize = 17.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(vertical = 19.dp)
        )
    }
}