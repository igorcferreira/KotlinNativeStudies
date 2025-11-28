package dev.igorcferreira.sharepointembedded.di.module

import dev.igorcferreira.sharepointembedded.AppViewModel
import dev.igorcferreira.sharepointembedded.ItemPreviewViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AppViewModel(get())
    }
    viewModel {
        ItemPreviewViewModel(get())
    }
}
