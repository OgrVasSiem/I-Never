package com.foresko.gamenever.core.google

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.foresko.gamenever.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AuthResult : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun createIntent(context: Context, input: Int): Intent =
        signInGoogleAccount(context = context).signInIntent.putExtra("input", input)

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(intent)
    }

    private fun signInGoogleAccount(context: Context): GoogleSignInClient {
        val signInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.server_client_id)).requestEmail().build()

        return GoogleSignIn.getClient(context, signInOption)
    }
}