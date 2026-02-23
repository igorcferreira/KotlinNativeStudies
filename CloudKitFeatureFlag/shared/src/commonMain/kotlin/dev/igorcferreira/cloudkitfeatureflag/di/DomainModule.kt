package dev.igorcferreira.cloudkitfeatureflag.di

import com.arkanakeys.ArkanaKeys
import dev.igorcferreira.cloudkitfeatureflag.domain.logic.AppFeatureManager
import dev.igorcferreira.cloudkitfeatureflag.domain.mapper.AppFeatureMapper
import dev.igorcferreira.cloudkitfeatureflag.domain.mapper.DomainMapper
import dev.igorcferreira.cloudkitfeatureflag.domain.repository.AppFeatureRepository
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitIntField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val domainModule = module {
    factory<DomainMapper<Map<String, CloudKitIntField>, AppFeatures>> {
        AppFeatureMapper()
    }
    factory<AppFeatureRepository> {
        AppFeatureRepository(
            container = ArkanaKeys.Global.container,
            recordName = ArkanaKeys.Global.recordName,
            cloudKitFeatureRepository = get(),
            mapper = get(),
        )
    }
    single<AppFeatureManager> {
        AppFeatureManager(
            appFeatureRepository = get(),
            coroutineScope = CoroutineScope(SupervisorJob()),
        )
    }
}
