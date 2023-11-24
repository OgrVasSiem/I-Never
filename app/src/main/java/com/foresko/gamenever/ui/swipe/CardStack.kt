package com.foresko.gamenever.ui.swipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.foresko.gamenever.core.rest.GameModel
import com.foresko.gamenever.core.utils.triggerVibration
import com.foresko.gamenever.ui.destination.game.GameViewModel
import com.foresko.gamenever.ui.destination.game.ProfileCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    items: List<GameModel> = viewModel.displayList,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    velocityThreshold: Dp = 125.dp,
    onCardSwiped: () -> Unit,
    offsets: List<MutableState<Dp>>,
) {
    val cardStackController = rememberCardStackController()

    val context = LocalContext.current

    cardStackController.onSwipeLeft = {
        triggerVibration(context)
        onCardSwiped()
    }

    cardStackController.onSwipeRight = {
        triggerVibration(context)
        onCardSwiped()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        val stack = createRef()

        Box(
            modifier = modifier
                .constrainAs(stack) {
                    top.linkTo(parent.top)
                }
                .draggableStack(
                    controller = cardStackController,
                    thresholdConfig = thresholdConfig,
                    velocityThreshold = velocityThreshold,
                )
                .fillMaxHeight()
        ) {
            items.asReversed().forEach { questionWithCard ->
                key(questionWithCard.question) {

                    val cardIndex = items.indexOf(questionWithCard)

                    val offsetY by animateDpAsState(
                        targetValue = offsets[cardIndex].value,
                        label = "offsetYAnimation"
                    )

                    val scaleFactor by animateFloatAsState(
                        targetValue = when (cardIndex) {
                            0 -> 1f
                            1 -> 0.9f
                            2 -> 0.8f
                            3 -> 0.7f
                            else -> 1f
                        },
                        label = "scaleAnimation"
                    )

                    AnimatedVisibility(
                        visible = items.contains(questionWithCard),
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        ProfileCard(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = offsetY)
                                .scale(scaleFactor)
                                .moveTo(
                                    x = if (cardIndex == 0) cardStackController.offsetX.value else 0f,
                                    y = if (cardIndex == 0) cardStackController.offsetY.value else 0f
                                ),
                            questionWithCard = questionWithCard
                        )
                    }
                }
            }
        }
    }
}