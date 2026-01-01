package com.alwinsden.dino.sheets.authentication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import com.alwinsden.dino.BuildKonfig
import com.alwinsden.dino.utilities.UI.Defaults
import com.alwinsden.dino.utilities.UI.DialogLoader
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.android_light_sq_ctn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

private const val TAG = "ContinueWithGoogleAndroid"

@Composable
actual fun TriggerAutoSignIn() {
}

actual suspend fun manualTriggerSignIn() {}


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
                    Log.d(TAG, googleIdTokenCredential.idToken)
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

@Composable
actual fun ClickableContinueWithGoogle(nonce: String) {
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)


    val customScope = rememberCoroutineScope()
    var loaderState by remember { mutableStateOf(false) }

    //this is used for automated bottom-sheet login
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
        .setServerClientId(BuildKonfig.CLIENT_ID_GOOGLE_AUTH)
        .setNonce(nonce)
        .build()

    //this is used for manual click-triggered login
    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
        serverClientId = BuildKonfig.CLIENT_ID_GOOGLE_AUTH
    )
        .setNonce(nonce)
        .build()
    LaunchedEffect(nonce) {
        if (nonce == Defaults.default) return@LaunchedEffect
        //auto login flow
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        loaderState = true
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            Log.i(TAG, "Triggered Google Sign in success")
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Error getting credential", e)
        } finally {
            loaderState = false
        }
    }
    Image(
        painter = painterResource(
            resource = Res.drawable.android_light_sq_ctn
        ), contentDescription = "Continue with Google",
        modifier = Modifier.clickable {
            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()
            customScope.launch {
                loaderState = true
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context
                    )
                    Log.i(TAG, "Triggered Google Sign in success")
                    handleSignIn(result)
                } catch (e: GetCredentialException) {
                    Log.e(TAG, "Error getting credential", e)
                } finally {
                    loaderState = false
                }
            }
        }
    )
    DialogLoader(loaderState)
}