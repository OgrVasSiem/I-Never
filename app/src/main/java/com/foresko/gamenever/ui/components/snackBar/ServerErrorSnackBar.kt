package com.foresko.gamenever.ui.components.snackBar

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.foresko.gamenever.R
import com.foresko.gamenever.ui.theme.INeverTheme
import kotlinx.coroutines.delay

@Composable
fun ServerErrorSnackBar(
    isServerError: Boolean,
    changeServerErrorState: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.error) }

    LaunchedEffect(key1 = isServerError) {
        if (isServerError) {
            mediaPlayer.start()

            delay(3000)

            changeServerErrorState()
        }
    }

    if (isServerError) {
        Box(
            modifier = modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
                .background(Color.Transparent),
        ) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = INeverTheme.colors.destructive,
                action = {
                    IconButton(
                        onClick = changeServerErrorState,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            tint = INeverTheme.colors.white
                        )
                    }
                },
                content = {
                    Text(
                        text = stringResource(R.string.server_error),
                        color = INeverTheme.colors.white,
                        style = INeverTheme.textStyles.body
                    )
                }

            )
        }
    }
}