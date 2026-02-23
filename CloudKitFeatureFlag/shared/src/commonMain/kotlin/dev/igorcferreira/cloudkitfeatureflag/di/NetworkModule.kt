package dev.igorcferreira.cloudkitfeatureflag.di

import dev.igorcferreira.cloudkitfeatureflag.network.repository.CloudKitFeatureRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<Json> { Json {
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
