package dev.igorcferreira.cloudkitfeatureflag.di

import com.arkanakeys.ArkanaKeys
import dev.igorcferreira.cloudkitfeatureflag.domain.logic.AppFeatureManager
import dev.igorcferreira.cloudkitfeatureflag.domain.mapper.AppFeatureMapper
import dev.igorcferreira.cloudkitfeatureflag.domain.mapper.DomainMapper
import dev.igorcferreira.cloudkitfeatureflag.domain.repository.AppFeatureRepository
import dev.igorcferreira.cloudkitfeatureflag.domain.repository.FileRepository
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitIntField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okio.FileSystem
import okio.SYSTEM
import org.koin.dsl.module

expect fun fetchRootFilePath(): String

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
    factory<FileRepository> {
        FileRepository(
            fileSystem = FileSystem.SYSTEM,
            rootPath = fetchRootFilePath(),
        )
    }
    single<AppFeatureManager> {
        AppFeatureManager(
            appFeatureRepository = get(),
            fileRepository = get(),
            json = get(),
            coroutineScope = CoroutineScope(SupervisorJob()),
        )
    }
}
