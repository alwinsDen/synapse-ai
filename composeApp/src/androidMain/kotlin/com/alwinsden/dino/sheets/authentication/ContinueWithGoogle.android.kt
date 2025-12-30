package com.alwinsden.dino.sheets.authentication

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.alwinsden.dino.BuildKonfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.coroutineScope

private const val TAG = "ContinueWithGoogleAndroid"

@Composable
actual fun TriggerAutoSignIn() {
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
        .setServerClientId(BuildKonfig.CLIENT_ID_GOOGLE_AUTH)
        .setNonce("test-nonce")
        .build()

    LaunchedEffect(Unit) {
        //auto login flow
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        coroutineScope {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                Log.i(TAG, "Triggered Google Sign in success")
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Error getting credential", e)
            }
        }
    }
}

fun handleSignIn(credsRequest: GetCredentialResponse) {
    val credsType = credsRequest.credential
    val responseJson: String
    when (credsType) {
        is PublicKeyCredential -> {
        }

        is PasswordCredential -> {
        }

        is CustomCredential -> {
            if (credsType.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credsType.data)
                    println(googleIdTokenCredential.idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(TAG, "Error parsing Google ID token", e)
                }
            }
        }

        else -> {
            println("Unknown credential type")
        }
    }
}