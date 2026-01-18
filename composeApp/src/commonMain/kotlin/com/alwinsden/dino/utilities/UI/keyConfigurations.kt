package com.alwinsden.dino.utilities.UI

import com.alwinsden.dino.BuildKonfig
import com.alwinsden.dino.requestManager.IClientInterface

class ClientKtorConfiguration : IClientInterface {
    override val baseUrl: String = BuildKonfig.KTOR_ENTRY_URL
}

object Defaults {
    val default = "DEFAULT"
}

object PageDefaults {
    //BotTextField
    val botTextDefault = "INIT"
    val botTextExtend = "EXTEND"
}