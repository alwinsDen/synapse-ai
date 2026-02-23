package com.alwinsden.dino.googleAuthn.serverManager

import com.alwinsden.dino.valkeyManager.ValkeyManager
import glide.api.models.GlideString.gs
import glide.api.models.commands.SetOptions
import io.ktor.server.application.*
import kotlinx.coroutines.future.await
import java.util.*

suspend fun ApplicationCall.nonceGenerator(): String {
    val generatedUuid = UUID.randomUUID().toString()

    val valkeyOptions = SetOptions.builder().expiry(SetOptions.Expiry.Seconds(60)).build()
    ValkeyManager.getClient().set(gs(generatedUuid), gs(generatedUuid), valkeyOptions).await()

    return generatedUuid
}