package dev.igorcferreira.cloudkitfeatureflag.di

import dev.igorcferreira.cloudkitfeatureflag.network.repository.CloudKitFeatureRepository
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val networkModule = module {
    factory<Json> { Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
    } }
    factory<Logger> {
        Logger.SIMPLE
    }
    factory<LogLevel> {
        LogLevel.ALL
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
    factory<CloudKitFeatureRepository> {
        CloudKitFeatureRepository(
            httpClient = get()
        )
    }
}
