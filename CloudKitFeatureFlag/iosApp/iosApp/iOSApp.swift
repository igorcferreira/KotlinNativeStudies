import SwiftUI
import Shared

@main
struct iOSApp: App {
    let manager: AppFeatureManager = {
        let koin = DIHelper.companion.buildBridge()
        return koin.manager
    }()
    
    var body: some Scene {
        WindowGroup {
            ContentView(appFeatures: manager.state)
        }
    }
}
