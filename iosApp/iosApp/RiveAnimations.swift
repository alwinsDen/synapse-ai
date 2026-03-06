//
//  RiveAnimations.swift
//  iosApp
//
//  Created by alwin on 06/03/26.
//
import ComposeApp
import SwiftUI

@_spi(RiveExperimental) import RiveRuntime

struct MartyView: View {
    var body: some View {
        AsyncRiveUIViewRepresentable {
            let worker = try await Worker()
            let file = try await File(source: .local("marty_v2", Bundle.main), worker: worker)
            return try await Rive(file: file)
        }
    }
}


class RiveNativeIosViewFactory : NativeViewFactory {
    func createMartyView() -> UIViewController {
        return UIHostingController(rootView: MartyView())
    }
}
