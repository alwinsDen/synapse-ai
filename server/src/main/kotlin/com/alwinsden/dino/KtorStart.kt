package com.alwinsden.dino

import com.alwinsden.dino.googleAuthn.serverManager.nonceGenerator
import com.alwinsden.dino.googleAuthn.serverManager.verifyGoogleToken
import com.alwinsden.dino.requestManager.utils.CustomInAppException
import com.alwinsden.dino.requestManager.utils.ErrorObjectCustom
import com.alwinsden.dino.requestManager.utils.ErrorTypeEnums
import com.alwinsden.dino.valkeyManager.ValkeyManager
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    //section to manage installations
    install(StatusPages) {
        //in-app custom error handler
        exception<CustomInAppException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorObjectCustom(
                    errorCode = cause.appCode,
                    errorType = ErrorTypeEnums.CUSTOM.name
                )
            )
        }

        //handle verification cases
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorObjectCustom(
                    errorMessage = "$cause",
                    errorType = ErrorTypeEnums.UNCONTROLLED_EXCEPTION.name
                )
            )
        }

        //handle illegal failures
        exception<IllegalStateException> { call, cause ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorObjectCustom(
                    errorMessage = "$cause",
                    errorType = ErrorTypeEnums.UNCONTROLLED_STATE.name
                )
            )
        }

        //any other error handling flow
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorObjectCustom(
                    errorMessage = "$cause",
                    errorType = ErrorTypeEnums.UNCONTROLLED_THROWABLE.name

                )
            )
        }
    }

    launch {
        try {
            val applicationEnv: ApplicationEnvironment = environment
            ValkeyManager.apply { initValkey(applicationEnv) }
        } catch (e: Exception) {
            log.error("Failed to initialize ValkeyManager")
            e.printStackTrace()
        }
    }
    routing {
        //health check API
        get("/health") {
            application.log.debug("Triggered health check")
            call.respondText("Ktor is healthy.")
        }

        //generate nonce
        get("/generate-nonce") {
            val generatedNonce = call.nonceGenerator()
            call.respondText(generatedNonce)
        }

        //g-auth google verify
        post("/login") {
            val text = call.receiveText()
            call.verifyGoogleToken(text)
            call.respondText("return JTW token here")//TODO
        }
    }
}