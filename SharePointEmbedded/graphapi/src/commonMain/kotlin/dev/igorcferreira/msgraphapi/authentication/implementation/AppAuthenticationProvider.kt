package dev.igorcferreira.msgraphapi.authentication.implementation

import dev.igorcferreira.msgraphapi.authentication.AuthenticationError
import dev.igorcferreira.msgraphapi.authentication.TokenProvider
import dev.igorcferreira.msgraphapi.authentication.model.OAuthToken
import dev.igorcferreira.msgraphapi.exception.GraphAPIException
import dev.igorcferreira.msgraphapi.exception.MSAuthenticationException
import dev.igorcferreira.msgraphapi.network.NetworkException
import dev.igorcferreira.msgraphapi.network.raise
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class AppAuthenticationProvider(
    private val tenantId: String,
    private val clientId: String,
    private val clientSecret: (clientId: String) -> String,
): TokenProvider {
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    @Throws(NetworkException::class, AuthenticationError::class, Exception::class)
    override suspend fun getToken(): String {
        try {
            val response = client.post {
                url("https://login.microsoftonline.com/$tenantId/oauth2/v2.0/token")
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(FormDataContent(Parameters.build {
                    append("client_id", clientId)
                    append("client_secret", clientSecret(clientId))
                    append("scope", "https://graph.microsoft.com/.default")
                    append("grant_type", "client_credentials")
                }))
            }

            if (!response.status.isSuccess()) {
                throw response.raise<MSAuthenticationException>()
            }

            val token: OAuthToken = response.body()
            return token.accessToken
        } catch (ex: Exception) {
            throw when(ex) {
                is NetworkException,
                is GraphAPIException,
                is MSAuthenticationException -> ex
                else -> NetworkException(HttpStatusCode.InternalServerError)
            }
        }
    }

    @Throws(NetworkException::class, AuthenticationError::class, Exception::class)
    override suspend fun signOut() = Unit
}
