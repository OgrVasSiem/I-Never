package com.foresko.gamenever.ui.destination.onboarding.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.foresko.gamenever.ui.theme.INeverTheme

@Composable
fun Onboarding(
    @DrawableRes image: Int,
    @StringRes info: Int,
    @StringRes infoDescription: Int,
    @StringRes buttonName: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = image)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9F)
                .aspectRatio(0.9f),
            contentScale = ContentScale.Fit
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(info),
                fontSize = 26.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight(700),
                color = INeverTheme.colors.primary,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(infoDescription),
                fontSize = 17.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight(400),
                color = INeverTheme.colors.primary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(INeverTheme.colors.accent)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    onClick()
                }
        ) {
            Text(
                text = stringResource(buttonName),
                fontSize = 17.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight(600),
                color = INeverTheme.colors.white,
            )
        }
    }
}