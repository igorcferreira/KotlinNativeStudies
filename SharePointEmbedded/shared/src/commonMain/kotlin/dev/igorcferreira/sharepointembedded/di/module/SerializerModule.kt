package dev.igorcferreira.sharepointembedded.di.module

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val serializerModule = module {
    factory<Json> {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
}
