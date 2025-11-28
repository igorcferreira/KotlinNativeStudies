package dev.igorcferreira.sharepointembedded.di

import dev.igorcferreira.sharepointembedded.di.module.networkModule
import dev.igorcferreira.sharepointembedded.di.module.viewModelModule
import org.koin.core.context.startKoin

class DIHelper {
    companion object {
        fun initKoin() = startKoin {
            modules(networkModule, viewModelModule)
        }
    }
}
