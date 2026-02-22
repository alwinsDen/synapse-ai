package com.alwinsden.dino.authentication.components

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import com.alwinsden.dino.requestManager.buildConfigSecrets
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

private const val TAG = "GoogleAuthProvider"

/**
 * Android implementation of GoogleAuthProvider using the modern Credential Manager API.
 */
class AndroidGoogleAuthProvider(
    private val context: Context,
    private val credentialManager: CredentialManager
) : GoogleAuthProvider {

    override suspend fun signIn(nonce: String): Result<String> {
        return try {
            val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
                serverClientId = buildConfigSecrets.sharedClientGoogleId
            )
                .setNonce(nonce)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            Log.i(TAG, "Manual Google Sign-in success")
            extractGoogleIdToken(result)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Error getting credential", e)
            Result.failure(e)
        }
    }

    override suspend fun checkExistingCredentials(nonce: String): String? {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setAutoSelectEnabled(true)
                .setServerClientId(buildConfigSecrets.sharedClientGoogleId)
                .setNonce(nonce)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            Log.i(TAG, "Auto Google Sign-in success")
            extractGoogleIdToken(result).getOrNull()
        } catch (e: GetCredentialException) {
            Log.d(TAG, "No existing credentials found", e)
            null
        }
    }

    private fun extractGoogleIdToken(response: GetCredentialResponse): Result<String> {
        return when (val credential = response.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        Result.success(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Error parsing Google ID token", e)
                        Result.failure(e)
                    }
                } else {
                    Result.failure(IllegalStateException("Unexpected credential type: ${credential.type}"))
                }
            }

            is PublicKeyCredential -> {
                Result.failure(IllegalStateException("PublicKeyCredential not supported"))
            }

            is PasswordCredential -> {
                Result.failure(IllegalStateException("PasswordCredential not supported"))
            }

            else -> {
                Result.failure(IllegalStateException("Unknown credential type"))
            }
        }
    }

    override suspend fun logoutFromGoogle() {
        TODO("NEED TO IMPLEMENT FOR ANDROID.")
    }
}

@Composable
actual fun rememberGoogleAuthProvider(): GoogleAuthProvider {
    val context = LocalContext.current
    return remember {
        AndroidGoogleAuthProvider(
            context = context,
            credentialManager = CredentialManager.create(context)
        )
    }
}
