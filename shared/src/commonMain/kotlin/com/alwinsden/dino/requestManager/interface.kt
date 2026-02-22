package com.alwinsden.dino.requestManager

import com.alwinsden.dino.BuildKonfig

interface IClientInterface {
    val baseUrl: String
}

class ClientKtorConfiguration : IClientInterface {
    override val baseUrl: String = buildConfigSecrets.sharedKtorEntryUrl
}

object buildConfigSecrets {
    val sharedKtorEntryUrl = BuildKonfig.KTOR_ENTRY_URL
    val sharedClientGoogleId = BuildKonfig.CLIENT_ID_GOOGLE_AUTH
}