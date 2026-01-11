package com.alwinsden.dino.requestManager.utils

import kotlinx.serialization.Serializable

class CustomErrorClasses {
}

class CustomInAppException(val appCode: Int) : RuntimeException()

@Serializable
data class ErrorObjectCustom(
    val errorCode: Int? = null,
    val errorType: String,
    val errorMessage: String? = null
)