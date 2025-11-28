package dev.igorcferreira.msgraphapi.model

import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailSet(
    val id: String,
    val large: Thumbnail,
    val medium: Thumbnail,
    val small: Thumbnail,
    val source: Thumbnail
)
