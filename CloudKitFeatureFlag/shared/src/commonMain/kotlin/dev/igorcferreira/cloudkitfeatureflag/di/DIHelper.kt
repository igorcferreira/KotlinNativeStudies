package dev.igorcferreira.cloudkitfeatureflag.di

import dev.igorcferreira.cloudkitfeatureflag.domain.logic.AppFeatureManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

class DIHelper {
    class KoinBridge: KoinComponent {
        val manager: AppFeatureManager
            get() = get<AppFeatureManager>()
    }

    @OptIn(ExperimentalObjCRefinement::class)
    companion object {
        val MODULES = listOf(
            domainModule,
            networkModule,
        )

        @HiddenFromObjC
        fun initKoin() = startKoin {
            modules(MODULES)
        }

        fun buildBridge(): KoinBridge {
            initKoin()
            return KoinBridge()
        }
    }
}
