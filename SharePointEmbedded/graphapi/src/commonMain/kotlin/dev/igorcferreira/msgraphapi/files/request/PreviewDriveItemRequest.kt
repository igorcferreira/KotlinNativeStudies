package dev.igorcferreira.msgraphapi.files.request

import dev.igorcferreira.msgraphapi.files.response.DriveItemPreview
import dev.igorcferreira.msgraphapi.network.RequestWithJsonBody
import io.ktor.http.*
import kotlinx.serialization.Serializable

class PreviewDriveItemRequest(
    driveId: String,
    itemId: String,
    parameters: Parameters = Parameters()
): RequestWithJsonBody<PreviewDriveItemRequest.Parameters?, DriveItemPreview>(
    path = "/drives/$driveId/items/$itemId/preview",
    body = parameters
) {
    override val method: HttpMethod = HttpMethod.Post

    @Serializable
    data class Parameters(
        val page: Int? = null,
        val zoom: Int? = null
    )
}
