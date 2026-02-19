package dev.igorcferreira.sharepointembedded.di.module

import dev.igorcferreira.sharepointembedded.AppViewModel
import dev.igorcferreira.sharepointembedded.ItemPreviewViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val viewModelModule = module {
    viewModel<AppViewModel>()
    viewModel<ItemPreviewViewModel>()
}
