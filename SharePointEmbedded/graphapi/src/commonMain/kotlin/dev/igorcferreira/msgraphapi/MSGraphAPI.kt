package dev.igorcferreira.msgraphapi

import dev.igorcferreira.msgraphapi.authentication.TokenProvider
import dev.igorcferreira.msgraphapi.authentication.implementation.AppAuthenticationProvider
import dev.igorcferreira.msgraphapi.network.Network
import dev.igorcferreira.msgraphapi.network.NetworkException
import dev.igorcferreira.msgraphapi.network.Request

class MSGraphAPI(
    baseUrl: String = BASE_URL,
    appTokenProvider: TokenProvider
) {
    private val network = Network(baseUrl, appTokenProvider)

    constructor(
        baseUrl: String = BASE_URL,
        tenantId: String,
        clientId: String,
        clientSecret: (clientId: String) -> String
    ): this(baseUrl, AppAuthenticationProvider(tenantId, clientId, clientSecret))

    @Throws(NetworkException::class, Exception::class)
    internal suspend inline fun <reified Response> perform(
        request: Request<Response>
    ) = network.perform<Response>(request)

    private companion object {
        const val BASE_URL = "https://graph.microsoft.com"
    }
}
