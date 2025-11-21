package dev.igorcferreira.msgraphapi.authentication.application

import dev.igorcferreira.msgraphapi.authentication.OAuthToken
import dev.igorcferreira.msgraphapi.authentication.TokenProvider
import dev.igorcferreira.msgraphapi.network.Network
import dev.igorcferreira.msgraphapi.network.provideHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.isSuccess

class AppAuthenticationProvider(
    tenantId: String,
    private val clientID: String,
    private val clientSecret: (clientId: String) -> String,
    private val httpClient: HttpClient = provideHttpClient()
): TokenProvider {

    private val tokenUrl = "https://login.microsoftonline.com/$tenantId/oauth2/v2.0/token"

    @Throws(Exception::class, Network.NetworkException::class)
    override suspend fun getAccessToken(): String {

        val response = httpClient.post {
            url(tokenUrl)
            headers {
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.FormUrlEncoded)
            }
            setBody(FormDataContent(Parameters.build {
                append("client_id", clientID)
                append("client_secret", clientSecret(clientID))
                append("scope", "https://graph.microsoft.com/.default")
                append("grant_type", "client_credentials")
            }))
        }

        if (!response.status.isSuccess()) {
            throw Network.NetworkException(response.status)
        }

        val token = response.body<OAuthToken>()
        return token.accessToken
    }

    @Throws(Exception::class, Network.NetworkException::class)
    override suspend fun signOut() {}
}
