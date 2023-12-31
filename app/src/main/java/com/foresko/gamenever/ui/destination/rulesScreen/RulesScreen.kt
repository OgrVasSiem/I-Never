package com.foresko.gamenever.ui.destination.rulesScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amplitude.api.Amplitude
import com.foresko.gamenever.R
import com.foresko.gamenever.core.utils.triggerVibration
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.RootNavigator
import com.foresko.gamenever.ui.theme.INeverTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(style = DestinationStyleBottomSheet::class)
@RootNavGraph
fun RulesScreen(
    rootNavigator: RootNavigator
) {
    Amplitude.getInstance().logEvent("rules_screen")

    RulesScreenContent(
        popBackStack = { rootNavigator.popBackStack() },
    )

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded
    )

    LaunchedEffect(modalBottomSheetState) {
        snapshotFlow { modalBottomSheetState.currentValue }
            .filter { it == ModalBottomSheetValue.Hidden }
            .collect {
                Amplitude.getInstance().logEvent("rules_close_scroll")
            }
    }
}

@Composable
fun RulesScreenContent(
    popBackStack: () -> Unit,
) {
    val smallScreen = LocalConfiguration.current.screenHeightDp.dp < 650.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = INeverTheme.colors.white),
    ) {
        Scaffold(
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .background(Color.Transparent),
            topBar = { TopAppBar(popBackStack = popBackStack) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color.White)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.Start
            ) {
                RuleItem(
                    icon = painterResource(R.drawable.ic_one),
                    text = stringResource(id = R.string.rules_text1)
                )

                if (!smallScreen) { Spacer(modifier = Modifier.height(45.dp)) }
                else Spacer(modifier = Modifier.height(20.dp))

                RuleItem(
                    icon = painterResource(R.drawable.ic_two),
                    text = stringResource(id = R.string.rules_text2)
                )

                if (!smallScreen) { Spacer(modifier = Modifier.height(45.dp)) }
                else Spacer(modifier = Modifier.height(20.dp))

                RuleItem(
                    icon = painterResource(R.drawable.ic_three),
                    text = stringResource(id = R.string.rules_text3)
                )

                if (!smallScreen) { Spacer(modifier = Modifier.height(45.dp)) }
                else Spacer(modifier = Modifier.height(20.dp))

                RuleItem(
                    icon = painterResource(R.drawable.ic_four),
                    text = stringResource(id = R.string.rules_text4)
                )

                if (!smallScreen) { Spacer(modifier = Modifier.height(45.dp)) }
                else Spacer(modifier = Modifier.height(20.dp))

                RuleItem(
                    icon = painterResource(R.drawable.ic_five),
                    text = stringResource(id = R.string.rules_text5)
                )
            }
        }
    }
}

@Composable
fun RuleItem(
    icon: Painter,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = icon,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.rulesText,
            modifier = Modifier.offset(y = (4).dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    popBackStack: () -> Unit
) {
    val context = LocalContext.current

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            androidx.compose.material.Text(
                stringResource(id = R.string.rules),
                style = INeverTheme.textStyles.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            androidx.compose.material.Icon(
                painter = painterResource(R.drawable.ic_close),
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
                                .logEvent("rules_close_button")
                            popBackStack()
                        }
                    ),
                tint = Color.Unspecified
            )
        }
    )
}