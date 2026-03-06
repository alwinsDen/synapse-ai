package com.alwinsden.dino.swiftBridge

import platform.UIKit.UIViewController

interface GoogleAuthenticatorIos {
    fun iosLogin(nonce: String, completion: (String?) -> Unit)
    fun iosCheckExisting(completion: (String?) -> Unit)
    fun iosGoogleLogout()
}


interface NativeViewFactory {
    fun createMartyView(): UIViewController
}