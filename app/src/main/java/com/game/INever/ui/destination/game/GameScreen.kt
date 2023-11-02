package com.game.INever.ui.destination.game

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
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.game.INever.R
import com.game.INever.core.rest.GameModel
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.destinations.destinations.PremiumScreenDestination
import com.game.INever.ui.swipe.CardStack
import com.game.INever.ui.theme.INeverTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.StateFlow
import okhttp3.internal.wait

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

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    navigateToPremiumScreen = { rootNavigator.navigate(PremiumScreenDestination) },
                    popBackStack = { rootNavigator.popBackStack() },
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                CardStack(
                    items = viewModel.displayList,
                    onCardSwiped = { viewModel.onCardSwiped() },
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
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(),
                text = questionWithCard.question,
                color = INeverTheme.colors.white,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.align(Alignment.End),
                text = questionWithCard.categoryName,
                color = INeverTheme.colors.white,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )
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


