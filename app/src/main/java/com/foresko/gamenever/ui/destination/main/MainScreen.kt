package com.foresko.gamenever.ui.destination.main

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.gamenever.R
import com.foresko.gamenever.core.utils.LocalActivity
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.RootNavigator
import com.foresko.gamenever.ui.destination.game.GameScreenNavArgs
import com.foresko.gamenever.ui.destination.main.components.MainCard
import com.foresko.gamenever.ui.destinations.destinations.GameScreenDestination
import com.foresko.gamenever.ui.destinations.destinations.RulesScreenDestination
import com.foresko.gamenever.ui.destinations.destinations.SettingsScreenDestination
import com.foresko.gamenever.ui.theme.INeverTheme
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
        navigateToGameScreen = { ids: List<Long>, fromAd: Boolean ->
            rootNavigator.navigate(
                GameScreenDestination(
                    navArgs = GameScreenNavArgs(
                        ids = ids.toLongArray(),
                        fromAd = fromAd
                    )
                )
            )
        },
        viewModel = viewModel,
    )
}

@Composable
fun MainScreenContent(
    navigateToSettingsScreen: () -> Unit,
    navigateToRulesScreen: () -> Unit,
    navigateToGameScreen: (ids: List<Long>, fromAd: Boolean) -> Unit,
    viewModel: MainViewModel,
) {
    val cards by viewModel.card.collectAsState()

    val cardStates by viewModel.cardStates.collectAsState()

    val activeCards = cardStates.filter { it.isSelected.value }.mapNotNull { it.cardData.value }

    val questionCounts by viewModel.questionCounts.collectAsState()

    val activity = LocalActivity.current

    val activeCardsCount = cardStates.count { it.isSelected.value }

    val textResource = when {
        activeCardsCount == 1 -> R.string.selected_cards_info
        activeCardsCount > 4 -> R.string.selected_cards_info3
        else -> R.string.selected_cards_info2
    }

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
                                        card = card,
                                        isPremiumActive = viewModel.premiumIsActive ?: false,
                                        cardState = cardStates.find { it.cardData.value == card }!!,
                                        questionsCount = questionCounts,
                                        showAds = {
                                            viewModel.showAds(activity) {
                                                navigateToGameScreen(listOf(card.id), true)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (activeCardsCount > 0) {
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }

                viewModel.loadQuestionsForActiveCards(activeCards)

                val totalQuestions = viewModel.questions.value.size

                if (activeCardsCount > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 20.dp)
                            .align(Alignment.BottomCenter)
                            .clip(RoundedCornerShape(20.dp))
                            .background(INeverTheme.colors.accent)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = rememberRipple(bounded = false),
                            ) {
                                navigateToGameScreen(activeCards.map { it.id }, false)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 7.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.game),
                                color = INeverTheme.colors.white,
                                style = INeverTheme.textStyles.boldButtonText
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = stringResource(
                                    textResource,
                                    activeCardsCount,
                                    totalQuestions
                                ),
                                color = INeverTheme.colors.white.copy(alpha = 0.7f),
                                style = INeverTheme.textStyles.subButtonText
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
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
        }
    )
}

