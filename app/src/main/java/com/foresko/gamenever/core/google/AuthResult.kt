package com.foresko.gamenever.core.google

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.foresko.gamenever.R
import com.foresko.gamenever.application.core.command.CommandDispatcher
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignInWithGoogleCommand
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AuthResult(
    private val commandDispatcher: CommandDispatcher
) : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun createIntent(context: Context, input: Int): Intent =
        signInGoogleAccount(context = context).signInIntent.putExtra("input", input)

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount> {
        return when (resultCode) {
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> GoogleSignIn.getSignedInAccountFromIntent(intent)
        }
    }

    private fun signInGoogleAccount(context: Context): GoogleSignInClient {
        val signInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.server_client_id)).requestEmail().build()

        return GoogleSignIn.getClient(context, signInOption)
    }

    fun processTask(task: Task<GoogleSignInAccount>?) {
        coroutineScope.launch {
            try {
                task?.getResult(ApiException::class.java)?.let { accountInit ->
                    commandDispatcher.dispatch(
                        SignInWithGoogleCommand(accountInit.idToken.orEmpty())
                    )
                }
            } catch (e: ApiException) {
                Log.e(ContentValues.TAG, "ApiException = $e.statusCode")
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
}