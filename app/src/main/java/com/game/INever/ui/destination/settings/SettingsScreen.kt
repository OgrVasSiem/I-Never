package com.game.INever.ui.destination.settings

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amplitude.api.Amplitude
import com.game.INever.R
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.destinations.destinations.PremiumScreenDestination
import com.game.INever.ui.theme.INeverTheme
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
@RootNavGraph
fun SettingsScreen(
    rootNavigator: RootNavigator
) {
    var amplitudeInit by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = amplitudeInit) {
        if (amplitudeInit) {
            Amplitude.getInstance().logEvent("settings_screen")

            amplitudeInit = false
        }
    }

    SettingsScreen(
        popBackStack = { rootNavigator.popBackStack() },
        navigateToPremiumScreen = { rootNavigator.navigate(PremiumScreenDestination) },
        /*navigateToApplicationLanguagesScreen = {
            rootNavigator.navigate(ApplicationLanguageScreenDestination)
        }*/
    )
}

@Composable
private fun SettingsScreen(
    popBackStack: () -> Unit,
    navigateToPremiumScreen: () -> Unit,
) {
    val context = LocalContext.current

    val intentShareApp = remember {
        Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, Addresses.GooglePlayAddress)
            this.type = "text/plain"
        }
    }

    val intentGooglePlay = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(Addresses.GooglePlayAddress)
        )
    }

    val intentTelegram = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(Addresses.NonPremiumTelegramAddress))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = INeverTheme.colors.white),
    ) {
        Scaffold(
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .background(Color.Transparent)
                .navigationBarsPadding()
                .statusBarsPadding(),
            topBar = { TopAppBar(popBackStack = popBackStack) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                PremiumButton(navigateToPremiumScreen = navigateToPremiumScreen)

                Spacer(modifier = Modifier.height(14.dp))

                DefaultSettingsButton(
                    text = R.string.write_in_support,
                    image = (R.drawable.ic_support),
                    onClick = { context.startActivity(intentTelegram) }
                )

                /*DefaultSettingsButton(
                    text = R.string.share_application,
                    image = (R.drawable.ic_language),
                    onClick = { *//*navigateToApplicationLanguagesScreen*//* }
                )*/

                DefaultSettingsButton(
                    text = R.string.estimate_application,
                    image = (R.drawable.ic_star),
                    onClick = { context.startActivity(intentGooglePlay) }
                )

                DefaultSettingsButton(
                    text = R.string.share_application,
                    image = (R.drawable.ic_share),
                    onClick = { context.startActivity(intentShareApp) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    popBackStack: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            Text(
                stringResource(id = R.string.settings),
                style = INeverTheme.textStyles.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 18.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false),
                        onClick = popBackStack
                    ),
                tint = Color.Unspecified
            )
        },
    )
}

@Composable
private fun PremiumButton(
    navigateToPremiumScreen: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 107.dp)
            .clip(RoundedCornerShape(24.dp))
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
            .background(INeverTheme.colors.button.copy(alpha = 0.20f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                navigateToPremiumScreen()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, true)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_premium),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = stringResource(R.string.premium_eng).uppercase(),
                        color = INeverTheme.colors.primary,
                        fontSize = 21.sp,
                        lineHeight = 23.sp,
                        fontWeight = FontWeight(700)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.more_info_about_premium),
                        color = INeverTheme.colors.primary.copy(alpha = 0.7f),
                        style = INeverTheme.textStyles.body2.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            )
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .defaultMinSize(minHeight = 46.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .border(1.dp, INeverTheme.colors.accent, RoundedCornerShape(24.dp))
                    .background(INeverTheme.colors.accent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        navigateToPremiumScreen()
                    }
            ) {
                Text(
                    text = stringResource(R.string.more),
                    color = INeverTheme.colors.white,
                    style = INeverTheme.textStyles.body,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 7.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun DefaultSettingsButton(
    @StringRes text: Int,
    @DrawableRes image: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true, color = INeverTheme.colors.accent)
            ) {
                try {
                    onClick()
                } catch (ex: Exception) {
                    FirebaseCrashlytics
                        .getInstance()
                        .recordException(ex)
                }
            }
            .padding(horizontal = 20.dp)
            .defaultMinSize(minHeight = 72.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center)

        ) {
            Icon(
                painter = painterResource(image),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Text(
                text = stringResource(text),
                color = INeverTheme.colors.primary,
                style = INeverTheme.textStyles.button,
                modifier = Modifier.padding(start = 12.dp, top = 3.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 10.dp)
            )
        }

        Divider(
            color = INeverTheme.colors.accent.copy(alpha = 0.40f),
            thickness = 0.75f.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

private object Addresses {
    const val NonPremiumTelegramAddress = "https://t.me/Foresko_Support"

    const val GooglePlayAddress =
        "https://play.google.com/store/apps/details?id=com.foresko.horoscopes"
}