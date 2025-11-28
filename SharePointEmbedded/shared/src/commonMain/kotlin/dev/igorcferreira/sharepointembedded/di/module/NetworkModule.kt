package dev.igorcferreira.sharepointembedded.di.module

import com.arkanakeys.ArkanaKeys
import dev.igorcferreira.msgraphapi.MSGraphAPI
import org.koin.dsl.module

val networkModule = module {
    single {
        MSGraphAPI(
            tenantId = ArkanaKeys.Global.tenantId,
            clientId = ArkanaKeys.Global.clientId,
            clientSecret = { ArkanaKeys.Global.clientSecret }
        )
    }
}
