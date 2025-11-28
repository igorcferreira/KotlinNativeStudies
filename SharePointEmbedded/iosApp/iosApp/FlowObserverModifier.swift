//
//  FlowObserverModifier.swift
//  iosApp
//
//  Created by Igor Ferreira on 24/11/25.
//
import Combine
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesCombine
import SwiftUI
import Shared

struct FlowObserverModifier<Output, Setter, Failure: Error, Object: SwiftUI.View>: ViewModifier {
    @State private var bag: (any Cancellable)? = nil
    @State private var target: Object
    private let nativeFlow: NativeFlow<Output, Failure, KotlinUnit>
    private let keyValue: ReferenceWritableKeyPath<Object, Setter>
    private let mapper: (Output) -> Setter
    
    init(
        nativeFlow: @escaping NativeFlow<Output, Failure, KotlinUnit>,
        target: Object,
        keyValue: ReferenceWritableKeyPath<Object, Setter>,
        mapper: @escaping (Output) -> Setter
    ) {
        self.nativeFlow = nativeFlow
        self.target = target
        self.keyValue = keyValue
        self.mapper = mapper
    }
    
    func body(content: Content) -> some View {
        content.onAppear {
            let publisher = createPublisher(for: nativeFlow)
            bag = publisher.sink(
                receiveCompletion: { _ in },
                receiveValue: { @MainActor value in
                    target[keyPath: keyValue] = mapper(value)
                })
        }.onDisappear {
            bag?.cancel()
        }
    }
}

extension View {
    @ViewBuilder
    func collecting<Output, Failure: Error, Object: SwiftUI.View>(
        nativeFlow: @escaping NativeFlow<Output, Failure, KotlinUnit>,
        into target: Object,
        state keyPath: ReferenceWritableKeyPath<Object, Output>
    ) -> some View {
        self.modifier(FlowObserverModifier<Output, Output, Failure, Object>(
            nativeFlow: nativeFlow,
            target: target,
            keyValue: keyPath,
            mapper: { $0 }
        ))
    }
    
    @ViewBuilder
    func collecting<Output, Setter, Failure: Error, Object: SwiftUI.View>(
        nativeFlow: @escaping NativeFlow<Output, Failure, KotlinUnit>,
        into target: Object,
        state keyPath: ReferenceWritableKeyPath<Object, Setter>,
        mapper: @escaping (Output) -> Setter
    ) -> some View {
        self.modifier(FlowObserverModifier<Output, Setter, Failure, Object>(
            nativeFlow: nativeFlow,
            target: target,
            keyValue: keyPath,
            mapper: mapper
        ))
    }
    
    @ViewBuilder
    func collecting<Output, Setter, Failure: Error, Object: SwiftUI.View>(
        nativeFlow: @escaping NativeFlow<Output, Failure, KotlinUnit>,
        into target: Object,
        state keyPath: ReferenceWritableKeyPath<Object, Setter>,
        mapping: KeyPath<Output, Setter>
    ) -> some View {
        self.modifier(FlowObserverModifier<Output, Setter, Failure, Object>(
            nativeFlow: nativeFlow,
            target: target,
            keyValue: keyPath,
            mapper: { $0[keyPath: mapping] }
        ))
    }
}
