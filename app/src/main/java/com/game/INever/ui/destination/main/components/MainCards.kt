package com.game.INever.ui.destination.main.components

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
import com.game.INever.ui.utils.linearGradient
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.game.INever.R
import com.game.INever.core.rest.Cards
import com.game.INever.ui.theme.INeverTheme

@Composable
fun MainCard(
    cards: Cards,
    onAlertNeeded: () -> Unit,
    isPremiumActive: Boolean
) {
    val isSelected = remember { mutableStateOf(false) }

    val isFreeTopic = cards.freeTopic || isPremiumActive

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp.dp - (4 * 8.dp)) / 2)
            .defaultMinSize(minHeight = 206.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = if (isFreeTopic && isSelected.value) 3.dp else 1.dp,
                brush =
                    if (isFreeTopic && isSelected.value) Brush.linearGradient(INeverTheme.gradients.gradientBorder)
                    else  SolidColor(Color(0xFFF2F2F8)),
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color(android.graphics.Color.parseColor(cards.color)))
            .clickable(onClick = {
                if (isFreeTopic) {
                    isSelected.value = !isSelected.value
                } else {
                    onAlertNeeded()
                }
            })
    ) {
        CardsContent(
            cards = cards,
            premiumIsActive = isPremiumActive
        )
    }
}

@Composable
fun CardsContent(
    cards: Cards,
    premiumIsActive: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MainInfo(cards.name, cards.image)

        Spacer(Modifier.height(12.dp))

        BottomContent(cards.questions, cards.freeTopic, premiumIsActive = premiumIsActive)
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
    questions: List<String>,
    freeTopic: Boolean,
    premiumIsActive: Boolean
) {
    val numberOfQuestions = questions.size

    val iconResource = if (freeTopic || premiumIsActive) R.drawable.ic_circle else R.drawable.img_premium_crown

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$numberOfQuestions ${stringResource(id = R.string.cards)}",
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