//
//  PreviewView.swift
//  iosApp
//
//  Created by Igor Ferreira on 28/11/25.
//
import SwiftUI
import Shared
import WebKit

struct PreviewView: View {
    private let viewModel = DIBag.shared.itemPreviewViewModel
    private let item: GraphapiDriveItem
    @State private var error: String? = nil
    @State private var loading = false
    @State private var url: String? = nil
    
    init(item: GraphapiDriveItem) {
        self.item = item
    }
    
    var body: some View {
        VStack {
            if loading {
                ProgressView()
                    .frame(maxWidth: .infinity, alignment: .center)
                    .id(UUID())
            } else if let url {
                WebView(url: URL(string: url))
            } else {
                Text(error ?? item.name)
            }
        }
        .task {
            viewModel.load(item: item)
        }
        .collecting(nativeFlow: viewModel.previewUrlFlow, into: self, state: \.url)
        .collecting(nativeFlow: viewModel.loadingFlow, into: self, state: \.loading, mapper: { $0.boolValue })
        .collecting(nativeFlow: viewModel.errorFlow, into: self, state: \.error)
    }
}
