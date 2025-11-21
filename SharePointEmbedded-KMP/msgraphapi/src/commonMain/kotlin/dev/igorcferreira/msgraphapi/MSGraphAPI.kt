package dev.igorcferreira.msgraphapi

import dev.igorcferreira.msgraphapi.authentication.application.AppAuthenticationProvider
import dev.igorcferreira.msgraphapi.network.Network
import dev.igorcferreira.msgraphapi.network.Request

class MSGraphAPI internal constructor(
    internal val network: Network
) {
    constructor(
        tenantId: String,
        clientID: String,
        clientSecret: (clientId: String) -> String
    ): this(Network(
        appTokenProvider = AppAuthenticationProvider(tenantId, clientID, clientSecret)
    ))

    @Throws(Exception::class, Network.NetworkException::class)
    internal suspend inline fun <reified T> perform(
        request: Request<T>
    ): T = network.perform(request)
}
