package dev.igorcferreira.msgraphapi

import dev.igorcferreira.msgraphapi.authentication.TokenProvider
import dev.igorcferreira.msgraphapi.network.Network
import dev.igorcferreira.msgraphapi.network.NetworkException
import dev.igorcferreira.msgraphapi.network.Request
import io.ktor.client.*

class MSGraphAPI internal constructor(
    private val network: Network
) {
    constructor(
        baseUrl: String = BASE_URL,
        appTokenProvider: TokenProvider,
        client: HttpClient,
    ): this(Network(baseUrl, appTokenProvider, client))

    @Throws(NetworkException::class, Exception::class)
    internal suspend inline fun <reified Response> perform(
        request: Request<Response>
    ) = network.perform<Response>(request)

    private companion object {
        const val BASE_URL = "https://graph.microsoft.com"
    }
}
