import SwiftUI
import Shared

extension GraphapiDriveItem: @retroactive Identifiable {}

struct ContentView: View {
    private let viewModel: AppViewModel
    private let currentItem: GraphapiDriveItem?
    private let select: @MainActor @Sendable (GraphapiDriveItem) -> Void
    
    @State private var loading = false
    @State private var items: [GraphapiDriveItem] = []
    @State private var error: Error? = nil
    @State private var username: String? = nil
    @State private var hasPreviewItem: Bool = false

    init(
        item: GraphapiDriveItem? = nil,
        select: @escaping @MainActor @Sendable (GraphapiDriveItem) -> Void = { _ in },
        viewModel: AppViewModel = DIBag.shared.appViewModel
    ) {
        self.currentItem = item
        self.select = select
        self.viewModel = viewModel
    }
    
    var body: some View {
        List {
            Section {
                if loading {
                    ProgressView()
                        .frame(maxWidth: .infinity, alignment: .center)
                        .id(UUID())
                } else if items.isEmpty {
                    Text("Empty folder")
                }
                
                ForEach(items) { item in
                    Button {
                        select(item)
                    } label: {
                        HStack {
                            Image(systemName: item.isFolder ? "folder.fill" : "document.fill")
                                .tint(.accentColor)
                            VStack(alignment: .leading) {
                                Text(item.name)
                                if let user = item.createdBy.user ?? item.createdBy.application {
                                    Text("Created by: \(user.displayName)")
                                        .font(.caption2)
                                }
                            }
                        }
                    }
                }
            } header: {
                if let username {
                    Text(username)
                }
            } footer: {
                if let error {
                    Text("\(error.localizedDescription)")
                }
            }
        }
        .sheet(isPresented: $hasPreviewItem) {
            PreviewView(item: currentItem!)
        }
        .onChange(of: currentItem) { _, newItem in
            hasPreviewItem = (newItem != nil && newItem?.isFolder == false)
        }
        .task {
            viewModel.load(item: currentItem)
            viewModel.authenticate()
        }
        .collecting(nativeFlow: viewModel.loadingFlow, into: self, state: \.loading, mapper: { $0.boolValue })
        .collecting(nativeFlow: viewModel.listFlow, into: self, state: \.items)
        .collecting(nativeFlow: viewModel.usernameFlow, into: self, state: \.username)
        .collecting(nativeFlow: viewModel.errorFlow, into: self, state: \.error, mapper: { $0?.asError() })
    }
}
