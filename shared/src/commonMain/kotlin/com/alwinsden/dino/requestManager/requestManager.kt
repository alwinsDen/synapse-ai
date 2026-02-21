package com.alwinsden.dino.requestManager

import io.ktor.client.*
import io.ktor.client.plugins.*

class RequestManager(private val requestConfig: IClientInterface) {
    public val client = HttpClient() {
        defaultRequest {
            url(requestConfig.baseUrl)
        }
    }
}