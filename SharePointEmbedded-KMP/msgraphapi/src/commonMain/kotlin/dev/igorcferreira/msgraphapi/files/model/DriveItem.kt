package dev.igorcferreira.msgraphapi.files.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriveItem(
    val id: String,
    val name: String,
    val eTag: String,
    val webUrl: String,
    @SerialName("@microsoft.graph.downloadUrl")
    val downloadUrl: String? = null
)
