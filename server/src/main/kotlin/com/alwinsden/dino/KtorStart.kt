package com.alwinsden.dino

import com.alwinsden.dino.googleAuthn.serverManager.GoogleAuthService
import com.alwinsden.dino.googleAuthn.serverManager.nonceGenerator
import com.alwinsden.dino.googleAuthn.serverManager.tables.UserInfo
import com.alwinsden.dino.googleAuthn.serverManager.verifyGoogleToken
import com.alwinsden.dino.requestManager.Ktor.LoginRequest
import com.alwinsden.dino.requestManager.Ktor.LoginResponse
import com.alwinsden.dino.requestManager.utils.CustomInAppException
import com.alwinsden.dino.requestManager.utils.ErrorObjectCustom
import com.alwinsden.dino.requestManager.utils.ErrorString
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
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {

    //DB specification
    val dbUsername = environment.config.property("dinoBackend.postgres.DB_USERNAME").getString()
    val dbPassword = environment.config.property("dinoBackend.postgres.DB_PASSWORD").getString()
    val dbUrl = environment.config.property("dinoBackend.postgres.DB_URL").getString()

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUsername,
        password = dbPassword
    )

    transaction {
        /*
        * code inside this transaction should NEVER be commited.
        * This for DEV only, breaking DB changes.
        * */


        /*Below definitions are needed*/
        SchemaUtils.create(UserInfo)
    }

    install(ContentNegotiation) {
        json()
    }
    //section to manage installations
    install(StatusPages) {
        //in-app custom error handler
        exception<CustomInAppException> { call, cause ->
            val httpStatus = when (cause.appCode) {
                1000 -> HttpStatusCode.Unauthorized
                else -> HttpStatusCode.BadRequest
            }
            call.respond(
                httpStatus,
                ErrorObjectCustom(
                    errorCode = cause.appCode,
                    errorType = ErrorTypeEnums.CUSTOM.name,
                    errorMessage = cause.incomingErrorMessage
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
            GoogleAuthService.init(
                googleAudience = environment.config
                    .property("dinoBackend.googleAuth.GOOGLE_AUDIENCE")
                    .getString()
            )
        } catch (e: Exception) {
            log.error("Failed to initialize services at startup")
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
            val request = call.receive<LoginRequest>()
            val result = call.verifyGoogleToken(request.token)
            val status = if (result.userCreated) "user created" else "user already exists"
            call.respond(HttpStatusCode.OK, LoginResponse(status))
        }
    }
}