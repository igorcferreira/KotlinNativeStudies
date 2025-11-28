package dev.igorcferreira.sharepointembedded.di.module

import com.arkanakeys.ArkanaKeys
import org.koin.core.qualifier.named
import org.koin.dsl.module

val arkanaModule = module {
    factory<String>(named("ClientId")) {
        ArkanaKeys.Global.clientId
    }
    factory<String>(named("ClientSecret")) {
        ArkanaKeys.Global.clientSecret
    }
    factory<String>(named("ContainerId")) {
        ArkanaKeys.Global.containerId
    }
    factory<String>(named("TenantId")) {
        ArkanaKeys.Global.tenantId
    }
}
