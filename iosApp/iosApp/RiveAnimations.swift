//
//  RiveAnimations.swift
//  iosApp
//
//  Created by alwin on 06/03/26.
//
import ComposeApp
import SwiftUI

@_spi(RiveExperimental) import RiveRuntime

struct BasicRiveView: View {
    let backgroundColor : SwiftUI.Color
    let animatedFileSource : String
    var body: some View {
        AsyncRiveUIViewRepresentable {
            let worker = try await Worker()
                let file = try await File(source: .local(animatedFileSource, Bundle.main), worker: worker)
            return try await Rive(file: file)
        }
        .background(backgroundColor)
        .ignoresSafeArea()
    }
}


class RiveNativeIosViewFactory : NativeViewFactory {
    func createRiveView(backgroundColor: String, animatedFileSource: String) -> UIViewController {
        let riveRuntimeBackground = Color(hex: backgroundColor)
        let controller = UIHostingController(rootView: BasicRiveView(backgroundColor: riveRuntimeBackground, animatedFileSource: animatedFileSource))
        controller.view.isOpaque = false
        return controller
    }
}
