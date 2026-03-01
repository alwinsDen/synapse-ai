package com.alwinsden.dino.requestManager

import com.alwinsden.dino.getPlatform
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

class RequestManager(private val requestConfig: IClientInterface = ClientKtorConfiguration()) {
    public val client = HttpClient() {
        defaultRequest {
            url(requestConfig.baseUrl)

            /*
            * Added global-headers
            * */
            headers["X-OS-TYPE"] = getPlatform().name
            headers["X-OS-VERSION"] = getPlatform().operatingVersion
        }
        install(ContentNegotiation) {
            json()
        }
    }
}