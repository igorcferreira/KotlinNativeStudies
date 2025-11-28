package dev.igorcferreira.msgraphapi.exception

import kotlinx.serialization.Serializable

@Serializable
data class GraphAPIException(
    val code: String,
    override val message: String
): RuntimeException(message)
