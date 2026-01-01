package com.alwinsden.dino.googleAuthn.serverManager

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*

fun ApplicationCall.verifyGoogleToken(mobileGoogleIdToken: String) {
//    application.log.info(mobileGoogleIdToken)
    val transport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
    val verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
        transport, jsonFactory
    ).setAudience(
        listOf(
            application.environment.config.propertyOrNull("dinoBackend.googleAuth.googleAudience")?.getString()
        )
    ).build()
    val idToken: GoogleIdToken = verifier.verify(mobileGoogleIdToken)
    val payload: GoogleIdToken.Payload = idToken.payload
    println(payload.nonce)
}


//nonce generator
fun ApplicationCall.nonceGenerator(): String {
    return java.util.UUID.randomUUID().toString()
}