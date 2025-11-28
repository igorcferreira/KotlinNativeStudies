package dev.igorcferreira.msgraphapi.files

import dev.igorcferreira.msgraphapi.MSGraphAPI
import dev.igorcferreira.msgraphapi.files.request.ListDriveItemChildrenRequest
import dev.igorcferreira.msgraphapi.files.request.PreviewDriveItemRequest
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import dev.igorcferreira.msgraphapi.files.response.DriveItemPreview

suspend fun MSGraphAPI.listChildren(
    driveId: String,
    itemId: String? = null
): List<DriveItem> {
    val request = ListDriveItemChildrenRequest(driveId, itemId)
    val items = perform(request)
    return items.value
}

suspend fun MSGraphAPI.previewItem(
    driveId: String,
    itemId: String
): DriveItemPreview {
    val request = PreviewDriveItemRequest(driveId, itemId)
    return perform(request)
}
