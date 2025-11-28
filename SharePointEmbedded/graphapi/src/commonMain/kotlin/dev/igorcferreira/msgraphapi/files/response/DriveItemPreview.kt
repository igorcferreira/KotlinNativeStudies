package dev.igorcferreira.msgraphapi.files.response

import kotlinx.serialization.Serializable

@Serializable
data class DriveItemPreview(
    val getUrl: String,
    val postParameters: String? = null,
    val postUrl: String? = null
)
