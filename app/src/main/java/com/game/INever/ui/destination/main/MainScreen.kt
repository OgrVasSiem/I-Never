package com.game.INever.ui.destination.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.game.INever.R
import com.game.INever.ui.RootNavGraph
import androidx.compose.runtime.collectAsState
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.destination.main.components.MainCard
import com.game.INever.ui.destinations.destinations.RulesScreenDestination
import com.game.INever.ui.destinations.destinations.SettingsScreenDestination
import com.game.INever.ui.theme.INeverTheme
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
@RootNavGraph
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    MainScreenContent(
        navigateToSettingsScreen = {
            rootNavigator.navigate(
                SettingsScreenDestination()
            )
        },
        navigateToRulesScreen = {
            rootNavigator.navigate(
                RulesScreenDestination()
            )
        },
        viewModel = viewModel
    )
}

@Composable
fun MainScreenContent(
    navigateToSettingsScreen: () -> Unit,
    navigateToRulesScreen: () -> Unit,
    viewModel: MainViewModel
) {
    val cards by viewModel.offers.collectAsState()

    val premiumStatus = viewModel.premiumIsActive

    val onAlertNeeded: () -> Unit = {}

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            backgroundColor = Color.White,
            modifier = Modifier
                .background(Color.White)
                .navigationBarsPadding()
                .statusBarsPadding(),
            topBar = {
                TopAppBar(
                    navigateToSettingsScreen = navigateToSettingsScreen,
                    navigateToRulesScreen = navigateToRulesScreen
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                LazyColumn {
                    val chunks = cards?.chunked(2) ?: listOf()

                    items(chunks) { chunk ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            chunk.forEach { card ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp)
                                ) {
                                    MainCard(
                                        cards = card,
                                        onAlertNeeded = onAlertNeeded,
                                        isPremiumActive = premiumStatus ?: false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navigateToSettingsScreen: () -> Unit,
    navigateToRulesScreen: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            Text(
                stringResource(id = R.string.title),
                style = INeverTheme.textStyles.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 18.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false),
                        onClick = navigateToSettingsScreen
                    ),
                tint = Color.Unspecified
            )
        },
        actions = {
            Icon(
                painter = painterResource(R.drawable.ic_rules),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false),
                        onClick = navigateToRulesScreen
                    ),
                tint = Color.Unspecified
            )
        },
    )
}

