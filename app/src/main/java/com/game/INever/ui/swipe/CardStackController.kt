package com.game.INever.ui.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material.SwipeableDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CardStackController(
    val scope: CoroutineScope,
    private val screenWidth: Float,
    internal val animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
) {
    val right = Offset(screenWidth, 0f)
    val left = Offset(-screenWidth, 0f)
    val center = Offset(0f, 0f)

    var threshold = 0.0f

    val offsetX = Animatable(0f)
    val offsetY = Animatable(0f)
    val rotation = Animatable(0f)
    val scale = Animatable(0.8f)

    var onSwipeLeft: () -> Unit = {}
    var onSwipeRight: () -> Unit = {}
    fun swipeLeft() {
        scope.apply {
            launch {
                offsetX.animateTo(-screenWidth, animationSpec)



                scale.animateTo(1f, animationSpec)

                onSwipeLeft()

                launch {
                    offsetX.snapTo(center.x)
                }

                launch {
                    offsetY.snapTo(0f)
                }

                launch {
                    rotation.snapTo(0f)
                }

                launch {
                    scale.snapTo(0.8f)
                }
            }

            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun swipeRight() {
        scope.apply {
            launch {
                offsetX.animateTo(screenWidth, animationSpec)



                scale.animateTo(1f, animationSpec)

                onSwipeRight()

                launch {
                    offsetX.snapTo(center.x)
                }

                launch {
                    offsetY.snapTo(0f)
                }

                launch {
                    scale.snapTo(0.8f)
                }

                launch {
                    rotation.snapTo(0f)
                }
            }

            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun returnCenter() {
        scope.apply {
            launch {
                offsetX.animateTo(center.x, animationSpec)
            }

            launch {
                offsetY.animateTo(center.y, animationSpec)
            }

            launch {
                rotation.animateTo(0f, animationSpec)
            }

            launch {
                scale.animateTo(0.8f, animationSpec)
            }
        }
    }
}

@Composable
fun rememberCardStackController(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): CardStackController {
    val scope = rememberCoroutineScope()
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    return remember {
        CardStackController(
            scope = scope,
            screenWidth = screenWidth,
            animationSpec = animationSpec
        )
    }
}
