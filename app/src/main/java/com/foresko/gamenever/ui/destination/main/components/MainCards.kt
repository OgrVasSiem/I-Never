package com.foresko.gamenever.ui.destination.main.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import coil.compose.rememberAsyncImagePainter
import com.foresko.gamenever.R
import com.foresko.gamenever.core.rest.Card
import com.foresko.gamenever.ui.theme.INeverTheme
import com.foresko.gamenever.ui.utils.linearGradient

@Composable
fun MainCard(
    card: Card,
    questionsCount: Map<Long, Int>,
    isPremiumActive: Boolean,
    cardState: CardState,
    showAds: () -> Unit
) {
    val isFreeTopic = card.freeTopic || isPremiumActive

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp.dp - (4 * 8.dp)) / 2)
            .defaultMinSize(minHeight = 206.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = if (isFreeTopic && cardState.isSelected.value) 3.dp else 1.dp,
                brush =
                if (isFreeTopic && cardState.isSelected.value) Brush.linearGradient(INeverTheme.gradients.gradientBorder)
                else SolidColor(Color(0xFFF2F2F8)),
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color(android.graphics.Color.parseColor(card.color)))

            .clickable(onClick = {
                if (isFreeTopic) {
                    cardState.isSelected.value = !cardState.isSelected.value
                    cardState.cardData.value = card

                } else {
                    cardState.showDialog.value = true
                }
            })
    ) {
        CardsContent(
            card = card,
            questionsCount = questionsCount,
            premiumIsActive = isPremiumActive,
            isSelected = cardState.isSelected.value,
        )

        TestGameDialog(
            showAds = showAds,
            card = card,
            showDialog = cardState.showDialog
        )
    }
}

@Composable
fun CardsContent(
    card: Card,
    questionsCount: Map<Long, Int>,
    premiumIsActive: Boolean,
    isSelected: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MainInfo(
            card.name,
            card.image
        )

        Spacer(Modifier.height(12.dp))

        BottomContent(
            card = card,
            questionsCount = questionsCount,
            freeTopic = card.freeTopic,
            premiumIsActive = premiumIsActive,
            isSelected = isSelected
        )
    }
}

@Composable
private fun MainInfo(
    name: String,
    iconUri: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = rememberAsyncImagePainter(Uri.parse(iconUri)),
            contentDescription = null,
            modifier = Modifier
                .size(115.dp)
                .align(CenterHorizontally)
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = name,
            style = INeverTheme.textStyles.nameCards,
            color = INeverTheme.colors.textColor,
            modifier = Modifier.align(CenterHorizontally)
        )

        Spacer(Modifier.height(11.dp))

        Divider(
            color = INeverTheme.colors.divider.copy(alpha = 0.4f),
            thickness = 0.5f.dp,
        )
    }
}

@Composable
private fun BottomContent(
    card: Card,
    questionsCount: Map<Long, Int>,
    freeTopic: Boolean,
    premiumIsActive: Boolean,
    isSelected: Boolean
) {
    val iconResource = when {
        isSelected -> R.drawable.ic_active
        freeTopic || premiumIsActive -> R.drawable.ic_circle
        else -> R.drawable.img_premium_crown
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${questionsCount[card.id] ?: 0} ${stringResource(id = R.string.cards)}",

            style = INeverTheme.textStyles.cards,
            color = INeverTheme.colors.textColor
        )

        Spacer(modifier = Modifier.weight(1f))

        if (freeTopic) {
            Image(
                painter = painterResource(iconResource),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
            )
        } else {
            Image(
                painter = painterResource(iconResource),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
            )
        }
    }
}
