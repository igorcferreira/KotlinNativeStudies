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
    private let appFeatures: AppFeatures
    @State private var showContent = false
    
    init(appFeatures: AppFeatures, showContent: Bool = false) {
        self.appFeatures = appFeatures
        self.showContent = showContent
    }
    
    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation { showContent = !showContent }
            }

            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    Text("SwiftUI: \(Greeting().greet())")
                    FlagInformation(appFeatures: appFeatures)
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(appFeatures: AppFeatures.companion.empty)
    }
}
