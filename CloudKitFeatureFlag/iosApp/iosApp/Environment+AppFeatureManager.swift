//
// Created by Igor Ferreira on 25/2/26.
//
import Foundation
import SwiftUI
import Shared

extension AppFeatures: @retroactive Identifiable {}

let diBridge: DIHelper.KoinBridge = {
    DIHelper.companion.buildBridge()
}()

struct AppFeatureManagerKey: EnvironmentKey {
    static var defaultValue: AppFeatureManager {
        diBridge.manager
    }
}

extension  EnvironmentValues {
    var appFeatureManager: AppFeatureManager {
        get { self[AppFeatureManagerKey.self] }
        set { self[AppFeatureManagerKey.self] = newValue }
    }
}
