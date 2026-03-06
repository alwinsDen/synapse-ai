//
//  GoogleAuthentication.swift
//  iosApp
//
//  Created by alwin on 05/03/26.
//

import ComposeApp
import GoogleSignIn
import UIKit

class NativeIosGoogleAuthentication: GoogleAuthenticatorIos {
    weak var viewController: UIViewController?

    func iosLogin(nonce: String, completion: @escaping (String?)->Void ){
        guard let vc = viewController else { completion(nil); return; }
        var resultToken: String? = nil

        DispatchQueue.main.async {
            GIDSignIn.sharedInstance.signIn(
                withPresenting: vc,
                hint: nil,
                additionalScopes: nil,
                nonce: nonce
            ) { result, error in
                resultToken = error == nil ? result?.user.idToken?.tokenString : nil
            }
        }

        completion(resultToken)
    }

    func iosCheckExisting(completion: @escaping (String?) -> Void) {
        GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
            let token = (error == nil && user != nil) ? user?.idToken?.tokenString : nil
            completion(token)
        }
    }

    func iosGoogleLogout() {
        DispatchQueue.main.async {
            GIDSignIn.sharedInstance.signOut()
        }
    }
}
