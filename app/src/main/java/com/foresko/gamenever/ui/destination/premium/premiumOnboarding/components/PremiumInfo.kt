package com.foresko.gamenever.ui.destination.premium.premiumOnboarding.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.theme.INeverTheme

@Composable
fun PremiumInfo() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.img_premium_info_bg1),
            contentDescription = null,
            modifier = Modifier
                .size(if (screenWidth > 650.dp) 370.dp else 270.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.FillBounds,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.img_premium_onbording),
                contentDescription = null,
                modifier = Modifier
                    .size(if (screenWidth > 650.dp) 159.dp else 110.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Text(
                text = stringResource(R.string.horoscopes_premium),
                color = INeverTheme.colors.primary,
                fontSize = 30.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(700),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            if (screenWidth > 650.dp) {
                Spacer(modifier = Modifier.height(26.dp))
            }else {Spacer(modifier = Modifier.height(10.dp))}

            if (screenWidth > 650.dp) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 19.dp)
                ) {
                    Info(R.string.premium_info_second)

                    Spacer(modifier = Modifier.height(16.dp))

                    Info(R.string.premium_info_thirth)

                    Spacer(modifier = Modifier.height(16.dp))

                    Info(R.string.premium_info_first)
                }
            }else {
                Text(
                    text = stringResource(R.string.premium_info),
                    color = INeverTheme.colors.primary,
                    style = INeverTheme.textStyles.onboardingBody2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun Info(
    @StringRes info: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_keep),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.Top)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(info),
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.onboardingBody2
        )
    }
}