import SwiftUI
import Shared

@main
struct iOSApp: App {
    @Environment(\.appFeatureManager) var appFeatureManager
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    appFeatureManager.startRefresh()
                }
                .onDisappear {
                    appFeatureManager.stopRefresh()
                }
        }
    }
}
