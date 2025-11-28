package dev.igorcferreira.msgraphapi.model

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val sourceItemId: String,
    val height: Int,
    val width: Int,
    val url: String
)
