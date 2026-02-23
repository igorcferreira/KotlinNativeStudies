import SwiftUI
import Shared

extension AppFeatures: @retroactive Identifiable {}

@main
struct iOSApp: App {
    let diBridge: DIHelper.KoinBridge = {
        DIHelper.companion.buildBridge()
    }()
    var manager: AppFeatureManager {
        diBridge.manager
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView(appFeatures: manager.state)
                .onAppear {
                    manager.startRefresh()
                }
                .onDisappear {
                    manager.stopRefresh()
                }
        }
    }
}
