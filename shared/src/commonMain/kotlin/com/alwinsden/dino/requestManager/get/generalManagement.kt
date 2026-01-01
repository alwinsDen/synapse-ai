package com.alwinsden.dino.requestManager.get

import com.alwinsden.dino.requestManager.RequestManager
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun RequestManager.healthCheck(): String {
    val response = client.get("/health")
    println(response.bodyAsText())
    return "TRUE"
}

//function to get nonce
suspend fun RequestManager.createNonce(): String {
    val response = client.get("/generate-nonce")
    return response.bodyAsText()
}