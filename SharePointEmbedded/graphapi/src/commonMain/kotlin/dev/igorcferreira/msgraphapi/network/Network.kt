package dev.igorcferreira.msgraphapi.network

import dev.igorcferreira.msgraphapi.authentication.TokenProvider
import dev.igorcferreira.msgraphapi.exception.ErrorResponse
import dev.igorcferreira.msgraphapi.exception.GraphAPIException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal class Network(
    private val baseUrl: String,
    private val appTokenProvider: TokenProvider,
    private val client: HttpClient
) {

    @Throws(NetworkException::class, Exception::class)
    suspend inline fun <reified Response> perform(
        request: Request<Response>
    ): Response {
        val token = appTokenProvider.getToken()

        try {
            val response = client.request {
                url("$baseUrl/${request.version.path}${request.path}")
                method = request.method

                bearerAuth(token)

                parameters {
                    request.parameters.forEach { (key, value) ->
                        append(key, value)
                    }
                }

                if (request is RequestWithJsonBody<*, *> && request.body != null) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(request.body)
                } else if (request is RequestWithFileBody<*> && request.fileContent.isNotEmpty()) {
                    header(HttpHeaders.ContentType, request.fileContentType)
                    setBody(request.fileContent)
                }
            }

            if (!response.status.isSuccess()) {
                throw response.raiseException()
            }

            return response.body<Response>()
        } catch (ex: Exception) {
            throw when(ex) {
                is NetworkException,
                is GraphAPIException -> ex
                else -> NetworkException(
                    HttpStatusCode.InternalServerError,
                    ex.message ?: HttpStatusCode.InternalServerError.description
                )
            }
        }
    }
}

suspend fun HttpResponse.raiseException(): Exception = try {
    val errorResponse = body<ErrorResponse>()
    errorResponse.error
} catch (_: Exception) {
    NetworkException(status = status)
}

suspend inline fun <reified E: Exception> HttpResponse.raise(): Exception = try {
    body<E>()
} catch (_: Exception) {
    NetworkException(status = status)
}
