package dev.igorcferreira.msgraphapi.network

import dev.igorcferreira.msgraphapi.authentication.TokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.isSuccess
import io.ktor.http.parameters

internal class Network internal constructor(
    internal val appTokenProvider: TokenProvider,
    internal val httpClient: HttpClient = provideHttpClient()
) {
    class NetworkException(
        private val code: HttpStatusCode,
        message: String = code.description
    ) : RuntimeException(message)

    @Throws(Exception::class, NetworkException::class)
    internal suspend inline fun <reified T> perform(
        request: Request<T>
    ): T {
        val token = appTokenProvider.getAccessToken()

        val response = httpClient.request {
            url(buildUrl(request))
            method = request.httpMethod
            headers {
                accept(ContentType.Application.Json)
                bearerAuth(token)
                if (request is RequestWithFile<T>) {
                    append("Content-Type", request.contentType)
                } else if (request is RequestWithBody<*, T>) {
                    contentType(ContentType.Application.Json)
                }
            }
            parameters {
                request.parameters.forEach { (key, value) ->
                    append(key, value)
                }
            }
            (request as? RequestWithBody<*, T>)?.let {
                setBody(it.requestBody)
            }
        }

        if (!response.status.isSuccess()) {
            throw NetworkException(response.status)
        }

        return response.body<T>()
    }

    private fun buildUrl(request: Request<*>): String {
        val baseURL = when(request.apiVersion) {
            APIVersion.V1 -> "https://graph.microsoft.com/v1.0"
            APIVersion.BETA -> "https://graph.microsoft.com/beta"
        }
        return  "$baseURL${request.path}"
    }
}

internal fun provideHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
    install(HttpTimeout) {
        socketTimeoutMillis = 2 * 60 * 1000 // 2 minutes timeout for socket connection
        requestTimeoutMillis = 2 * 60 * 1000 // 2 minutes timeout for requests
        connectTimeoutMillis =
            20 * 1000 // 30 seconds timeout for establishing connection to server
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}
