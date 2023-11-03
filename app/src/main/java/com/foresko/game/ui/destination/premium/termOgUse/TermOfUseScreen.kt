package com.foresko.game.ui.destination.premium.termOgUse

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.foresko.game.R
import com.foresko.game.ui.RootNavGraph
import com.foresko.game.ui.RootNavigator
import com.foresko.game.ui.theme.INeverTheme
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
@RootNavGraph
fun TermOfUseScreen(
    rootNavigator: RootNavigator
) {
    TermOfUseScreen(
        popBackStack = { rootNavigator.popBackStack() }
    )
}

@Composable
private fun TermOfUseScreen(
    popBackStack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .navigationBarsPadding()
    ) {
        TopAppBar(popBackStack = popBackStack)

        TermOfUseWebViewPage()
    }
}

@Composable
private fun TopAppBar(
    popBackStack: () -> Unit
) {
    androidx.compose.material.TopAppBar(
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = popBackStack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = INeverTheme.colors.primary,
                )
            }

            Text(
                text = stringResource(R.string.term_of_use),
                fontSize = 17.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight(600),
                color = INeverTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun TermOfUseWebViewPage() {
    AndroidView(
        factory = {
            val authParams = "https://app.foresco.ru/terms.html"

            WebView(it).apply {
                loadUrl(authParams)
            }
        }
    )
}