import SwiftUI
import Shared

struct ContentView: View {
    private let greeting = Greeting()
    @State private var showContent = false
    @State private var text: String = ""

    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation {
                    showContent = !showContent
                }
            }

            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    Text("SwiftUI: \(text)")
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
        .task {
            text = (greeting.loadedDrives.listenTo {
                text = ($0 ?? "Empty") as String
            } ?? "Not loaded") as String
            try? await greeting.loadItems()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
