@file:Suppress("unused")

package dev.igorcferreira.sharepointembedded.di

import dev.igorcferreira.sharepointembedded.AppViewModel
import dev.igorcferreira.sharepointembedded.ItemPreviewViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DIBag: KoinComponent {
    val appViewModel: AppViewModel
        get() = get()
    val itemPreviewViewModel: ItemPreviewViewModel
        get() = get()
}

inline fun <reified T: Any> KoinComponent.get(): T = inject<T>().value
