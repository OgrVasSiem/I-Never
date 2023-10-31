package com.game.INever.ui.swipe

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.game.INever.core.rest.GameModel
import com.game.INever.ui.destination.game.ProfileCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    items: List<GameModel>,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    velocityThreshold: Dp = 125.dp,
    onSwipeLeft: (item: GameModel) -> Unit = {},
    onSwipeRight: (item: GameModel) -> Unit = {},
    onEmptyStack: (lastItem: GameModel) -> Unit = {},
    currentIndex: Int,
    onCardSwiped: () -> Unit,
    backgroundColor: Color
) {
    val cardStackController = rememberCardStackController()

    cardStackController.onSwipeLeft = {
        onSwipeLeft(items[currentIndex])
        onCardSwiped()
    }

    cardStackController.onSwipeRight = {
        onSwipeRight(items[currentIndex])
        onCardSwiped()
    }

    Log.d("index", "a = $items")

    Log.e("index", "b = $currentIndex")

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
                    velocityThreshold = velocityThreshold
                )
                .fillMaxHeight()
        ) {
            items.asReversed().forEachIndexed { index, questionWithCard ->
                ProfileCard(
                    modifier = Modifier
                        .moveTo(
                            x = if (index == currentIndex) cardStackController.offsetX.value else 0f,
                            y = if (index == currentIndex) cardStackController.offsetY.value else 0f
                        )
                        .visible(visible = index == currentIndex || index == currentIndex - 1)  // Изменяем условие для отображения карточек

                        .graphicsLayer(
                            rotationZ = if (index == currentIndex) cardStackController.rotation.value else 0f,
                            scaleX = if (index < currentIndex) cardStackController.scale.value else 1f,
                            scaleY = if (index < currentIndex) cardStackController.scale.value else 1f
                        ),
                    questionWithCard = questionWithCard,
                    index = backgroundColor
                )
            }
        }
    }
}
