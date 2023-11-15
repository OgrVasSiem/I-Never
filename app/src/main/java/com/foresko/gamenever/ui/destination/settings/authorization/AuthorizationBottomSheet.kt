package com.foresko.gamenever.ui.destination.settings.authorization

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.gamenever.R
import com.foresko.gamenever.core.utils.LocalModalBottomSheetState
import com.foresko.gamenever.ui.RootNavGraph
import com.foresko.gamenever.ui.theme.INeverTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(style = DestinationStyleBottomSheet::class)
@RootNavGraph
fun AuthorizationBottomSheet(
    viewModel: AuthorizationViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = LocalModalBottomSheetState.current

    BackHandler { coroutineScope.launch { sheetState.hide() } }

    AuthorizationBottomSheetContent(
        viewModel = viewModel,
        popBackStack = { coroutineScope.launch { sheetState.hide() } },
    )
}

@Composable
private fun AuthorizationBottomSheetContent(
    viewModel: AuthorizationViewModel,
    popBackStack: () -> Unit,

) {
    val authResultLauncher =
        rememberLauncherForActivityResult(contract = viewModel.authResult) { task ->
            viewModel.authorization(task = task)
        }

    ErrorToastLaunch(
        isNetworkConnectionError = viewModel.isNetworkConnectionError,
        changeNetworkConnectionErrorState = viewModel::changeNetworkConnectionErrorState
    )

    LaunchedEffect(key1 = viewModel.authorizationState) {
        delay(500)

        if (viewModel.authorizationState) {
            popBackStack()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .background(Color.White)
    ) {
        if (viewModel.session  != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        viewModel.signOutGoogleAccount()
                    }
                    .padding(horizontal = 20.dp, vertical = 22.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_log_out),
                    contentDescription = null,
                    tint = INeverTheme.colors.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.log_out_from_account),
                    color = INeverTheme.colors.primary,
                    style = INeverTheme.textStyles.body
                )
            }
        } else {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(R.drawable.img_cloud),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.authorization_in_Inever_game),
                color = INeverTheme.colors.primary,
                style = INeverTheme.textStyles.boldButtonText
            )

            Spacer(modifier = Modifier.height(40.dp))

            AuthorizationButton(
                openGoogleAlertDialog = { authResultLauncher.launch(signInRequestCode) },
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .border(
                        width = 1.dp,
                        INeverTheme.colors.placeholder,
                        RoundedCornerShape(100.dp)
                    )
                    .background(Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        popBackStack()
                    }
            ) {
                Text(
                    text = stringResource(R.string.not_now),
                    color = INeverTheme.colors.secondary,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(600)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun AuthorizationButton(
    openGoogleAlertDialog: () -> Unit,
    viewModel: AuthorizationViewModel
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(INeverTheme.colors.accent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true, color = INeverTheme.colors.white)
            ) {
                openGoogleAlertDialog()
                Log.d("InitializationProvider", "Account info: ${viewModel.session}")

            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = stringResource(R.string.enter_with_google),
                color = INeverTheme.colors.white,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600)
            )
        }
    }
}

@Composable
private fun ErrorToastLaunch(
    isNetworkConnectionError: Boolean,
    changeNetworkConnectionErrorState: (Boolean) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = isNetworkConnectionError) {
        if (isNetworkConnectionError) {
            Toast.makeText(
                context,
                context.getString(R.string.network_connection_error),
                Toast.LENGTH_SHORT
            ).show()

            delay(1000)

            changeNetworkConnectionErrorState(false)
        }
    }
}

private const val signInRequestCode = 10001