package com.alwinsden.dino.requestManager.Ktor

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val token: String)

@Serializable
data class LoginResponse(val status: String)