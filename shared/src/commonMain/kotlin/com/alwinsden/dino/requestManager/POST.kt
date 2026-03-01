package com.alwinsden.dino.requestManager

import com.alwinsden.dino.requestManager.Ktor.LoginRequest
import com.alwinsden.dino.requestManager.Ktor.LoginResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun RequestManager.googleLogin(googleToken: String, nonce: String) : LoginResponse{
    val response = client.request("/login"){
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        setBody(LoginRequest(token = googleToken, nonce = nonce))
    }
    println(response)
    return response.body<LoginResponse>()
}