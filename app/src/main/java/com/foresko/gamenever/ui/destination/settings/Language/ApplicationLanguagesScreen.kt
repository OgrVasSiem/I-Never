package com.foresko.gamenever.ui.destination.settings.Language

/*import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.game.INever.R
import com.game.INever.ui.RootNavGraph
import com.game.INever.ui.RootNavigator
import com.game.INever.ui.theme.INeverTheme
import com.game.INever.utils.LocalActivity

import java.util.Locale

@Composable
@Destination()
@RootNavGraph
fun ApplicationLanguageScreen(
    viewModel: ApplicationLanguageViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    ApplicationLanguageScreen(
        viewModel = viewModel,
        popBackStack = { rootNavigator.popBackStack() }
    )
}*/

/*
@Composable
private fun ApplicationLanguageScreen(
    viewModel: ApplicationLanguageViewModel,
    popBackStack: () -> Unit
) {
    val activity = LocalActivity.current
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        backgroundColor = INeverTheme.colors.bg,
        topBar = { TopAppBar(popBackStack = popBackStack) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            items(viewModel.languagesList) { language ->
                LanguageItem(
                    activeLanguage = viewModel.activeLanguage,
                    language = language
                ) {
                    viewModel.updateApplicationLanguage(
                        language = language,
                        activity = activity,
                        context = context
                    )
                }

                Divider(
                    color = INeverTheme.colors.divider,
                    thickness = 0.5f.dp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    popBackStack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.application_language),
                color = INeverTheme.colors.primary,
                style = INeverTheme.textStyles.bodySemiBold.copy(fontSize = 17.sp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = popBackStack
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                    tint = INeverTheme.colors.primary
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}

@Composable
private fun LanguageItem(
    activeLanguage: String,
    language: String,
    onClick: () -> Unit
) {
    val nameOnLanguage = remember(language) {
        val locale = Locale(language)

        locale.getDisplayName(locale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }

    val nameInAppLanguage = remember(language, activeLanguage) {
        val locale = Locale(language)

        locale.getDisplayName(Locale(activeLanguage))
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) { onClick() }
            .padding(horizontal = 20.dp)
            .defaultMinSize(minHeight = 68.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "$nameOnLanguage - $nameInAppLanguage",
            color = INeverTheme.colors.primary,
            style = INeverTheme.textStyles.body,
            modifier = Modifier
                .weight(1f, true)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painterResource(R.drawable.arrow_right),
            contentDescription = null,
            tint = if (language == activeLanguage) INeverTheme.colors.accent else Color.Transparent
        )
    }
}*/
