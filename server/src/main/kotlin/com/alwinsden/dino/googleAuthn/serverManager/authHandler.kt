package com.alwinsden.dino.googleAuthn.serverManager

import com.alwinsden.dino.googleAuthn.serverManager.tables.UserInfoDataClass
import com.alwinsden.dino.googleAuthn.serverManager.tables.UserInfoDbActions
import com.alwinsden.dino.requestManager.utils.CustomInAppException
import com.alwinsden.dino.requestManager.utils.ErrorString
import com.alwinsden.dino.valkeyManager.ValkeyManager
import glide.api.models.GlideString.gs
import io.ktor.server.application.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

data class GoogleTokenVerificationResult(
    val googleSubjectId: String,
    val userCreated: Boolean
)

suspend fun ApplicationCall.verifyGoogleToken(mobileGoogleIdToken: String): GoogleTokenVerificationResult {
    // Verify token using singleton service
    val payload = GoogleAuthService.verify(mobileGoogleIdToken)

    // Launch user existence check in parallel
    val userExistsChannel = Channel<Boolean>()
    launch {
        val userInfoDbActions = UserInfoDbActions()
        val exists = userInfoDbActions.userExists(payload.subject)
        userExistsChannel.send(exists)
    }

    // Validate nonce from Valkey (concurrent with user existence check)
    val cachedNonce = try {
        ValkeyManager.getClient().get(gs(payload.nonce)).get()
    } catch (e: Exception) {
        userExistsChannel.close()
        application.log.error("Valkey nonce retrieval failed: ${e.message}", e)
        throw CustomInAppException(appCode = 1002, incomingErrorMessage = ErrorString[1002]!! + e.message)
    }

    if (cachedNonce == null) {
        userExistsChannel.close()
        throw CustomInAppException(appCode = 1000, incomingErrorMessage = ErrorString[1000]!!)
    }
    application.log.debug("Verified")

    // Wait for user existence check to complete
    val userExists = userExistsChannel.receive()
    var userCreated = false

    if (!userExists) {
        val userInfoDbActions = UserInfoDbActions()
        userInfoDbActions.createNewUser(
            UserInfoDataClass(
                googleSubjectId = payload.subject,
                userFullName = payload["name"] as String,
                userEmail = payload.email,
                userGoogleProfile = payload["picture"] as String
            )
        )
        userCreated = true
    }

    return GoogleTokenVerificationResult(
        googleSubjectId = payload.subject,
        userCreated = userCreated
    )
}
