@file:Suppress("unused")
package dev.igorcferreira.msgraphapi.files.response

import dev.igorcferreira.msgraphapi.model.File
import dev.igorcferreira.msgraphapi.model.Folder
import dev.igorcferreira.msgraphapi.model.IdentitySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriveItem(
    val id: String,
    val name: String,
    val eTag: String,
    val webUrl: String,
    val createdBy: IdentitySet,
    val createdDateTime: String,
    val lastModifiedBy: IdentitySet? = null,
    val lastModifiedDateTime: String? = null,
    val folder: Folder? = null,
    val file: File? = null,
    @SerialName("@microsoft.graph.downloadUrl")
    val downloadUrl: String? = null,
    val cTag: String? = null,
) {
    val isFolder: Boolean
        get() = folder != null
}
