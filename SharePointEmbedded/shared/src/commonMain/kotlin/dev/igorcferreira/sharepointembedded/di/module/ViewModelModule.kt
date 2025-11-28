package dev.igorcferreira.sharepointembedded.di.module

import dev.igorcferreira.sharepointembedded.AppViewModel
import dev.igorcferreira.sharepointembedded.ItemPreviewViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<AppViewModel> {
        AppViewModel(
            graphAPI = get(),
            provider = get()
        )
    }
    viewModel<ItemPreviewViewModel> {
        ItemPreviewViewModel(
            graphAPI = get(),
            driveId = get(named("ContainerId")),
        )
    }
}
