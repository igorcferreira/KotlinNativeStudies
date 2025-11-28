package dev.igorcferreira.msgraphapi.network

import io.ktor.http.*

enum class GraphAPIVersion(
    internal val path: String
) {
    V1("v1.0"), BETA("beta")
}

open class Request<Response>(
    val path: String,
    open val method: HttpMethod = HttpMethod.Get,
    open val version: GraphAPIVersion = GraphAPIVersion.V1,
    open val parameters: Map<String, String> = emptyMap()
)

open class RequestWithJsonBody<Body, Response>(
    path: String,
    open val body: Body
): Request<Response>(path)

open class RequestWithFileBody<Response>(
    path: String,
    open val fileContent: ByteArray,
    open val fileContentType: ContentType = ContentType.Application.OctetStream
): Request<Response>(path)
