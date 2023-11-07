package com.foresko.game.ui.destination.game

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.game.R
import com.foresko.game.core.rest.GameModel
import com.foresko.game.ui.RootNavGraph
import com.foresko.game.ui.RootNavigator
import com.foresko.game.ui.destinations.destinations.PremiumScreenDestination
import com.foresko.game.ui.destinations.destinations.PrivacyPolicyScreenDestination
import com.foresko.game.ui.destinations.destinations.TermOfUseScreenDestination
import com.foresko.game.ui.swipe.CardStack
import com.foresko.game.ui.theme.INeverTheme
import com.foresko.game.utils.LocalActivity
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameScreenContent(
    rootNavigator: RootNavigator,
    viewModel: GameViewModel
) {
    val isLastCard by viewModel.isLastCard

    val activity = LocalActivity.current

    if (isLastCard) {
        UpgradeToPremiumDialog(
            showDialog = rememberSaveable { mutableStateOf(true) },
            navigateToPrivacyPolicy = { rootNavigator.navigate(PrivacyPolicyScreenDestination) },
            navigateToTermOfUse = { rootNavigator.navigate(TermOfUseScreenDestination) },
        )
    }

    LaunchedEffect(isLastCard) {
        Log.d("GameScreenContent", "UpgradeToPremiumDialog visibility changed: $isLastCard")
        if (isLastCard) {
            // Здесь можно добавить дополнительную логику, если нужно что-то сделать именно при показе диалога
            Log.d("GameScreenContent", "UpgradeToPremiumDialog is shown due to last card reached.")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    currentQuestionNumber = viewModel.currentQuestionNumber.intValue,
                    totalQuestions = viewModel.getInitialQuestionCount(),
                    navigateToPremiumScreen = { rootNavigator.navigate(PremiumScreenDestination) },
                    popBackStack = { rootNavigator.popBackStack() },
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                CardStack(
                    items = viewModel.displayList,
                    onCardSwiped = { viewModel.onCardSwiped(activity) },
                    offsets = viewModel.offsets,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier,
    questionWithCard: GameModel,
    scaleFactor: Float = 1f
) {
    val backgroundColor = Color(questionWithCard.colorInt ?: Color.Transparent.toArgb())
    Card(
        modifier = modifier
            .fillMaxSize()
            .scale(scaleFactor)
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 100.dp),
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .scale(scaleFactor)
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.title_game),
                color = INeverTheme.colors.white,
                style = INeverTheme.textStyles.titleCards
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = questionWithCard.question,
                color = INeverTheme.colors.white,
                style = INeverTheme.textStyles.textCards,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.align(Alignment.End),
                text = questionWithCard.categoryName.uppercase(),
                color = INeverTheme.colors.white.copy(alpha = 0.5f),
                style = INeverTheme.textStyles.body2
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    currentQuestionNumber: Int,
    totalQuestions: Int,
    navigateToPremiumScreen: () -> Unit,
    popBackStack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            val questionText = stringResource(
                id = R.string.question_format,
                currentQuestionNumber,
                totalQuestions
            )
            Text(
                text = questionText,
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