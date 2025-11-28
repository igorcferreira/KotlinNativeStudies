package dev.igorcferreira.msgraphapi.files.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriveItemList(
    val value: List<DriveItem>,
    @SerialName("@odata.nextLink")
    val nextLink: String? = null
)
