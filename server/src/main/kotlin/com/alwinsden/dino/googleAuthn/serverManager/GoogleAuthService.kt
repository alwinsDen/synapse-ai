package com.alwinsden.dino.googleAuthn.serverManager

import com.alwinsden.dino.requestManager.utils.CustomInAppException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory

object GoogleAuthService {
    private var verifier: GoogleIdTokenVerifier? = null

    fun init(googleAudience: String) {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        verifier = GoogleIdTokenVerifier.Builder(transport, GsonFactory.getDefaultInstance())
            .setAudience(listOf(googleAudience))
            .build()
    }

    fun verify(mobileGoogleIdToken: String): GoogleIdToken.Payload {
        val v = verifier ?: throw IllegalStateException("GoogleAuthService not initialized.")
        val idToken = try {
            v.verify(mobileGoogleIdToken)
        } catch (e: Exception) {
            throw CustomInAppException(appCode = 1001, incomingErrorMessage = e.message)
        }
        return idToken?.payload
            ?: throw CustomInAppException(appCode = 1001, incomingErrorMessage = "Token verification returned null.")
    }
}
