package com.alwinsden.dino.googleAuthn.serverManager

import com.alwinsden.dino.requestManager.utils.CustomInAppException
import com.alwinsden.dino.valkeyManager.ValkeyManager
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import glide.api.models.GlideString.gs
import glide.api.models.commands.SetOptions
import io.ktor.server.application.*
import kotlinx.coroutines.future.await
import java.util.*

fun ApplicationCall.verifyGoogleToken(mobileGoogleIdToken: String) {
    val transport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
    val verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
        transport, jsonFactory
    ).setAudience(
        listOf(
            application.environment.config.propertyOrNull("dinoBackend.googleAuth.GOOGLE_AUDIENCE")?.getString()
        )
    ).build()
    var idToken: GoogleIdToken?
    var payload: GoogleIdToken.Payload?
    try {
        idToken = verifier.verify(mobileGoogleIdToken)
        payload = idToken.payload
        println(payload.email)
    } catch (e: Exception) {
        throw CustomInAppException(appCode = 1001)
    }

    val cachedNonce = ValkeyManager.getClient().get(gs(payload?.nonce)).get()
    if (cachedNonce == null) {
        throw CustomInAppException(appCode = 1000)
    } else {
        application.log.debug("Verified ")
    }
}


//nonce generator
suspend fun ApplicationCall.nonceGenerator(): String {
    val generatedUuid = UUID.randomUUID().toString()

    //set up the object class to set Valkey nonce
    val valkeyOptions = SetOptions.builder().expiry(SetOptions.Expiry.Seconds(60)).build()
    ValkeyManager.getClient().set(gs(generatedUuid), gs(generatedUuid), valkeyOptions).await()

    return generatedUuid
}