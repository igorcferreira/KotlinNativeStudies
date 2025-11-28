package dev.igorcferreira.msgraphapi.model

import kotlinx.serialization.Serializable

@Serializable
data class Identity(
    val displayName: String,
    val id: String? = null,
    val tenantId: String? = null,
    val thumbnails: ThumbnailSet? = null
)
