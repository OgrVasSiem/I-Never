package com.foresko.gamenever.ui.destination.settings

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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.amplitude.api.Amplitude
import com.foresko.gamenever.R
import com.foresko.gamenever.core.utils.storeUrl
import com.foresko.gamenever.core.utils.triggerVibration
import com.foresko.gamenever.dataStore.Session
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.RootNavigator
import com.foresko.gamenever.ui.destinations.destinations.AuthorizationBottomSheetDestination
import com.foresko.gamenever.ui.destinations.destinations.PremiumScreenDestination
import com.foresko.gamenever.ui.launchSingleTopNavigate
import com.foresko.gamenever.ui.theme.INeverTheme
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ramcosta.composedestinations.annotation.Destination
import org.json.JSONObject

@Composable
@Destination
@RootNavGraph
fun SettingsScreen(
    rootNavigator: RootNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
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
        navigateToAuthorizationBottomSheet = {
            rootNavigator.launchSingleTopNavigate(AuthorizationBottomSheetDestination())
        },
        session = viewModel.session
    )
}

@Composable
private fun SettingsScreen(
    popBackStack: () -> Unit,
    navigateToPremiumScreen: () -> Unit,
    navigateToAuthorizationBottomSheet: () -> Unit,
    session: Session?
) {
    val context = LocalContext.current

    val intentShareApp = remember {
        Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, Uri.parse(storeUrl))
            this.type = "text/plain"
        }
    }

    val intentGooglePlay = remember {
        Intent(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(storeUrl)
            )
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

                DefaultSettingsButtonNew(
                    functionName = R.string.acc,
                    icon = (R.drawable.ic_authorization),
                    onClick = {
                        triggerVibration(context)
                        Amplitude.getInstance().logEvent("account")
                        navigateToAuthorizationBottomSheet()
                    },
                    session = session
                )

                DefaultSettingsButton(
                    text = R.string.write_in_support,
                    image = (R.drawable.ic_support),
                    onClick = {
                        triggerVibration(context)
                        Amplitude.getInstance().logEvent("support")
                        context.startActivity(intentTelegram)
                    }
                )

                DefaultSettingsButton(
                    text = R.string.estimate_application,
                    image = (R.drawable.ic_star),
                    onClick = {
                        triggerVibration(context)
                        Amplitude.getInstance().logEvent("rate_app")
                        context.startActivity(intentGooglePlay)
                    }
                )

                DefaultSettingsButton(
                    text = R.string.share_application,
                    image = (R.drawable.ic_share),
                    onClick = {
                        triggerVibration(context)
                        Amplitude.getInstance().logEvent("share_app")
                        context.startActivity(intentShareApp)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.version),
                    color = INeverTheme.colors.primary.copy(alpha = 0.65f),
                    style = INeverTheme.textStyles.footerSettings
                )

                Spacer(modifier = Modifier.height(14.dp))

                InfoAboutDevelopersCompany(modifier = Modifier.align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    popBackStack: () -> Unit
) {
    val context = LocalContext.current

    CenterAlignedTopAppBar(
        colors = topAppBarColors(containerColor = Color.White),
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
                        onClick = {
                            triggerVibration(context)
                            Amplitude
                                .getInstance()
                                .logEvent("settings_back_button")
                            popBackStack()
                        }
                    ),
                tint = Color.Unspecified
            )
        }
    )
}

@Composable
private fun PremiumButton(
    navigateToPremiumScreen: () -> Unit
) {
    val context = LocalContext.current

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
                triggerVibration(context)
                Amplitude
                    .getInstance()
                    .logEvent("premium_button")

                Amplitude.getInstance().logEvent("premium_screen", JSONObject().put("path", "settings_screen"))
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
                        triggerVibration(context)
                        Amplitude
                            .getInstance()
                            .logEvent("premium_button")
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
            color = INeverTheme.colors.divider.copy(alpha = 0.20f),
            thickness = 0.75f.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun DefaultSettingsButtonNew(
    @DrawableRes icon: Int,
    @StringRes functionName: Int,
    session: Session? = null,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
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
            .padding(horizontal = 20.dp, vertical = 22.dp)
            .fillMaxWidth()
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = INeverTheme.colors.accent
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f, true)
        ) {
            Text(
                text = stringResource(functionName),
                color = INeverTheme.colors.primary,
                style = INeverTheme.textStyles.body,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )

            if (session?.email != null) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = session.email ?: "",
                    color = INeverTheme.colors.primary.copy(alpha = 0.60f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight(400),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }

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
        color = INeverTheme.colors.divider.copy(alpha = 0.20f),
        thickness = 0.75f.dp,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

private object Addresses {
    const val NonPremiumTelegramAddress = "https://t.me/Foresko_Support"
}

@Composable
private fun InfoAboutDevelopersCompany(
    modifier: Modifier
) {
    val context = LocalContext.current

    val intent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://foresko.com/")
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        androidx.compose.material3.Text(
            text = stringResource(R.string.developed),
            fontSize = 13.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = INeverTheme.colors.primary,

            )

        Spacer(modifier = Modifier.width(6.dp))

        Icon(
            painter = painterResource(R.drawable.groop_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    triggerVibration(context)
                    Amplitude
                        .getInstance()
                        .logEvent("foresko")
                    context.startActivity(intent)
                }
        )
    }
}