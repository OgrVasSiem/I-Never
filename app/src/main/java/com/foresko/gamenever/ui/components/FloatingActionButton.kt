package com.foresko.gamenever.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionButton(
    modifier: Modifier = Modifier,
    background: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(CornerSize(percent = 50)),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    shadowColor: Color = Color.Black,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .shadow(
                elevation.elevation(interactionSource).value,
                shape,
                clip = false,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .background(background, shape)
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),

        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ExtendedFloatingActionButton(
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(CornerSize(percent = 50)),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 0.dp,
    endPadding: Dp = 0.dp,
    text: @Composable () -> Unit
) {
    FloatingActionButton(
        background = background,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = shape,
        elevation = elevation
    ) {
        Row(
            modifier = Modifier
                .padding(end = endPadding)
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()

                Spacer(modifier = Modifier.width(8.dp))
            }

            text()
        }
    }
}