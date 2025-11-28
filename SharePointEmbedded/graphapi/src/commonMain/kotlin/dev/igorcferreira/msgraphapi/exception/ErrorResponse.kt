package dev.igorcferreira.msgraphapi.exception

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: GraphAPIException,
    override val message: String = error.message
): RuntimeException(error.message)

@Serializable
data class MSAuthenticationException(
    val error: String,
    @SerialName("error_description")
    override val message: String,
): RuntimeException(message)
