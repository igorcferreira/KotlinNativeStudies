import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        DIHelper.companion.doInitKoin()
    }
    
    @State var path: [GraphapiDriveItem] = []
    @State var previewItem: GraphapiDriveItem? = nil
    @State var previwing: Bool = false
    
    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $path) {
                createList()
                    .navigationDestination(for: GraphapiDriveItem.self) { item in
                        createList(for: item)
                    }
            }
            .sheet(isPresented: $previwing) {
                NavigationStack {
                    PreviewView(item: previewItem!)
                        .navigationTitle(previewItem!.name)
                }
            }
            .onChange(of: previewItem) { _, newValue in
                previwing = newValue != nil
            }
            .onChange(of: previwing) { _, newValue in
                if (!newValue) { previewItem = nil }
            }
        }
    }
    
    @ViewBuilder
    func createList(for item: GraphapiDriveItem? = nil) -> some View {
        ContentView(item: item) { destination in
            if destination.isFolder {
                path.append(destination)
            } else {
                previewItem = destination
            }
        }
        .navigationTitle(item?.name ?? "SharePoint")
        .navigationBarTitleDisplayMode(.inline)
    }
}
