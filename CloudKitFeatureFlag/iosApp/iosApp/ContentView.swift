import Shared
import SwiftUI

extension LocalizedStringKey.StringInterpolation {
    mutating func appendInterpolation(_ value: Bool) {
        appendLiteral(value.description)
    }
}

struct FlagInformation: View {
    let appFeatures: AppFeatures
    
    var body: some View {
        VStack(spacing: 16) {
            Text("Feature A: \(appFeatures.featureA)")
            Text("Feature A: \(appFeatures.featureB)")
        }
    }
}

struct ContentView: View {
    @State private var showContent = false
    @Environment(\.appFeatureManager) var appFeatureManager
    
    init(showContent: Bool = false) {
        self.showContent = showContent
    }
    
    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation { showContent = !showContent }
            }

            if showContent {
                FlagInformation(appFeatures: appFeatureManager.state)
                    .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
