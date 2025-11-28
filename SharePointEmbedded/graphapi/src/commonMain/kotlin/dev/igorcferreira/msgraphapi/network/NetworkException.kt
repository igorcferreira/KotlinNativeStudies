package dev.igorcferreira.msgraphapi.network

import io.ktor.http.*

class NetworkException(
    val status: HttpStatusCode,
    message: String = status.description
) : RuntimeException(message)
