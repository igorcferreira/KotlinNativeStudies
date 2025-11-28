@file:Suppress("unused")

package dev.igorcferreira.sharepointembedded.di

import dev.igorcferreira.sharepointembedded.AppViewModel
import dev.igorcferreira.sharepointembedded.ItemPreviewViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object DIBag: KoinComponent {
    val appViewModel: AppViewModel
        get() = get<AppViewModel>()
    val itemPreviewViewModel: ItemPreviewViewModel
        get() = get<ItemPreviewViewModel>()
}
