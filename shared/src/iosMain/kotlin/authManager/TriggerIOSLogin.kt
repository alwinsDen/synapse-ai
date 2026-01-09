package authManager

import platform.AuthenticationServices.*
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.darwin.NSObject
import kotlin.coroutines.suspendCoroutine

class IOSAuthentication : NSObject(), ASAuthorizationControllerDelegateProtocol,
    ASAuthorizationControllerPresentationContextProvidingProtocol {
    suspend fun triggerLoginAtRequest() = suspendCoroutine<String?> { continuation ->
        val appleIdProvider = ASAuthorizationAppleIDProvider()
        val request = appleIdProvider.createRequest()
        request.requestedScopes = listOf(ASAuthorizationScopeFullName, ASAuthorizationScopeEmail)
        val authController = ASAuthorizationController(authorizationRequests = listOf(request))
        authController.delegate = this
        authController.presentationContextProvider = this
        authController.performRequests()
    }

    override fun presentationAnchorForAuthorizationController(controller: ASAuthorizationController): ASPresentationAnchor? {
        return UIApplication.sharedApplication.keyWindow ?: UIWindow()
    }
}