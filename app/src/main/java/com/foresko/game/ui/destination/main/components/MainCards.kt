package com.foresko.game.ui.destination.main.components

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.foresko.game.R
import com.foresko.game.core.rest.Card
import com.foresko.game.ui.RootNavigator
import com.foresko.game.ui.destinations.destinations.PrivacyPolicyScreenDestination
import com.foresko.game.ui.destinations.destinations.TermOfUseScreenDestination
import com.foresko.game.ui.theme.INeverTheme
import com.foresko.game.ui.utils.linearGradient

@Composable
fun MainCard(
    card: Card,
    questionsCount: Map<Long, Int>,
    isPremiumActive: Boolean,
    rootNavigator: RootNavigator,
    cardState: CardState,
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

        UpgradeToPremiumDialog(
            showDialog = cardState.showDialog,
            navigateToPrivacyPolicy = { rootNavigator.navigate(PrivacyPolicyScreenDestination) },
            navigateToTermOfUse = { rootNavigator.navigate(TermOfUseScreenDestination) },
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
        MainInfo(card.name, card.image)

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
                modifier = Modifier
                    .size(382.dp, 490.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.White)
                ) {
                    CloseButton(onDismissRequest = { showDialog.value = false })

                    CustomDialogContent(
                        navigateToTermOfUse = navigateToTermOfUse,
                        navigateToPrivacyPolicy = navigateToPrivacyPolicy
                    )
                }
            }
        }
    }
}

@Composable
fun CloseButton(onDismissRequest: () -> Unit) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier.fillMaxSize()
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
fun CustomDialogContent(
    navigateToTermOfUse: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
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

        Spacer(modifier = Modifier.height(350.dp))

        Info(
            navigateToTermOfUse = navigateToTermOfUse,
            navigateToPrivacyPolicy = navigateToPrivacyPolicy
        )
    }
}

@Composable
private fun Info(
    navigateToTermOfUse: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        InfoItem(
            text = R.string.privacy_policy_eng,
            onClick = navigateToPrivacyPolicy
        )

        InfoItem(
            text = R.string.term_of_use_eng,
            onClick = navigateToTermOfUse
        )
    }
}

@Composable
private fun InfoItem(
    @StringRes text: Int,
    onClick: () -> Unit
) {
    androidx.compose.material.Text(
        text = stringResource(text),
        color = INeverTheme.colors.primary.copy(alpha = 0.50f),
        fontSize = 12.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight(400),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    )
}

@Composable
private fun PremiumButton() {
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
            }
    ) {
        androidx.compose.material.Text(
            text = stringResource(R.string.try_free),
            color = INeverTheme.colors.white,
            fontSize = 17.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(horizontal = 36.dp)
        )
    }
}