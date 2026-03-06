import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let auth = NativeIosGoogleAuthentication()
        let vc = MainViewControllerKt.MainViewController(googleAuthenticator: auth)
        auth.viewController = vc
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}



