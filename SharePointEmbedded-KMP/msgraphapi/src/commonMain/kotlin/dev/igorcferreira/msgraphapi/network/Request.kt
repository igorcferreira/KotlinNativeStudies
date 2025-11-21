package dev.igorcferreira.msgraphapi.network

import io.ktor.http.HttpMethod

enum class APIVersion {
    V1, BETA
}

open class Request<Response>(
    open val httpMethod: HttpMethod = HttpMethod.Get,
    open val apiVersion: APIVersion = APIVersion.V1,
    open val parameters: Map<String, String> = emptyMap(),
    open val path: String
)

open class RequestWithBody<B, Response>(
    open val requestBody: B,
    path: String
): Request<Response>(path = path)

open class RequestWithFile<Response>(
    override val httpMethod: HttpMethod = HttpMethod.Post,
    open val contentType: String,
    body: ByteArray,
    path: String
): RequestWithBody<ByteArray, Response>(
    requestBody = body,
    path = path
)
