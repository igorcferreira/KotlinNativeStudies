package dev.igorcferreira.sharepointembedded.di

import dev.igorcferreira.sharepointembedded.di.module.arkanaModule
import dev.igorcferreira.sharepointembedded.di.module.networkModule
import dev.igorcferreira.sharepointembedded.di.module.serializerModule
import dev.igorcferreira.sharepointembedded.di.module.viewModelModule
import org.koin.core.context.startKoin

class DIHelper {
    companion object {
        val MODULES = listOf(
            arkanaModule,
            serializerModule,
            networkModule,
            viewModelModule
        )
        fun initKoin() = startKoin {
            modules(MODULES)
        }
    }
}
