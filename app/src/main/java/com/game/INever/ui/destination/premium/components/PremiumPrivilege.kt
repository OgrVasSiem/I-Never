package com.game.INever.ui.destination.premium.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game.INever.R
import com.game.INever.ui.theme.INeverTheme

@Composable
fun PremiumPrivilege(premiumIsActive: Boolean) {
    val title = remember(premiumIsActive) {
        if (premiumIsActive) R.string.available_to_you else R.string.what_you_get
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(INeverTheme.colors.white)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFB12A5B),
                        Color(0xFFCF556C),
                        Color(0xFFFF8C7F),
                        Color(0xFFFF8177)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(title),
                color = INeverTheme.colors.primary,
                fontSize = 20.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight(600)
            )

            Spacer(modifier = Modifier.height(12.dp))

            DefaultItem(
                icon = R.drawable.ic_keep,
                text = R.string.first_premium_privilege
            )

            DefaultItem(
                icon = R.drawable.ic_keep,
                text = R.string.second_premium_privilege
            )

            DefaultItem(
                icon = R.drawable.ic_keep,
                text = R.string.third_premium_privilege
            )

            DefaultItem(
                icon = R.drawable.ic_keep,
                text = R.string.fourth_premium_privilege
            )
        }
    }
}

@Composable
private fun DefaultItem(
    @DrawableRes icon: Int,
    @StringRes text: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(text),
            color = INeverTheme.colors.primary,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400)
        )
    }
}