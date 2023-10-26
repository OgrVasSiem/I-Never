package com.game.INever.ui.destination.game

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.game.INever.R
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.destinations.destinations.PremiumScreenDestination
import com.game.INever.ui.theme.INeverTheme
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination(navArgsDelegate = GameScreenNavArgs::class)
@RootNavGraph
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    rootNavigator: RootNavigator,

    ) {
    GameScreenContent(
        rootNavigator = rootNavigator,
        viewModel = viewModel
    )
}

@Composable
fun GameScreenContent(
    rootNavigator: RootNavigator,
    viewModel: GameViewModel
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .background(Color.White)
                .navigationBarsPadding()
                .statusBarsPadding(),
            topBar = {
                TopAppBar(
                    navigateToPremiumScreen = { rootNavigator.navigate(PremiumScreenDestination) },
                    popBackStack = { rootNavigator.popBackStack() },
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                SwipeCard(viewModel = viewModel)
            }
        }
    }
}


@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun SwipeCard(
    viewModel: GameViewModel = hiltViewModel()
) {
    val questions = viewModel.questions
    val states = List(questions.size) { rememberSwipeableCardState() }

    Box(
        Modifier
            .padding(24.dp)
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        for (index in questions.indices) {
            val state = states[index]
            if (state.swipedDirection == null) {
                ProfileCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .swipableCard(
                            state = state,
                            blockedDirections = listOf(Direction.Down),
                            onSwiped = {
                                viewModel.setNextQuestion()
                                if (index == questions.size - 1) {
                                    viewModel.currentPage.value += 1
                                }
                            },
                        ),
                    question = questions[index]
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navigateToPremiumScreen: () -> Unit,
    popBackStack: () -> Unit,
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
        actions = {
            Icon(
                painter = painterResource(R.drawable.crown_premium_dist),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false),
                        onClick = navigateToPremiumScreen
                    ),
                tint = Color.Unspecified
            )
        }
    )
}

@Composable
private fun ProfileCard(
    modifier: Modifier,
    question: String?
) {
    Card(
        modifier.background(Color.Red),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            Column(Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = question ?: "",
                    color = INeverTheme.colors.accent,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

