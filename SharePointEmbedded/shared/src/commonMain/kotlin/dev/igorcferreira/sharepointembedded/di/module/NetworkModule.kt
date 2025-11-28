package dev.igorcferreira.sharepointembedded.di.module

import dev.igorcferreira.msgraphapi.MSGraphAPI
import dev.igorcferreira.msgraphapi.authentication.MSAuthenticationProvider
import dev.igorcferreira.msgraphapi.authentication.TokenProvider
import dev.igorcferreira.msgraphapi.authentication.implementation.AppAuthenticationProvider
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    factory<MSGraphAPI> {
        MSGraphAPI(
            appTokenProvider = get(named("appTokenProvider")),
            client = get(),
        )
    }
    factory<TokenProvider>(named("appTokenProvider")) {
        AppAuthenticationProvider(
            tenantId = get(named("TenantId")),
            clientId = get(named("ClientId")),
            clientSecret = { get(named("ClientSecret")) },
            client = get()
        )
    }
    factory<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            install(Logging) {
                level = get()
                logger = get()
            }
        }
    }
    factory<Logger> {
        Logger.SIMPLE
    }
    factory<LogLevel> {
        LogLevel.ALL
    }
    factory<MSAuthenticationProvider> {
        MSAuthenticationProvider(
            tenantId = get(named("TenantId")),
            clientId = get(named("ClientId")),
            scopes = listOf("https://graph.microsoft.com/.default")
        )
    }
}
