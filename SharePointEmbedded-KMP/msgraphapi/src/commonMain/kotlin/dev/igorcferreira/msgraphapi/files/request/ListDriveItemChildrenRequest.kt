package dev.igorcferreira.msgraphapi.files.request

import dev.igorcferreira.msgraphapi.files.model.DriveItemList
import dev.igorcferreira.msgraphapi.network.Request

class ListDriveItemChildrenRequest(
    driveId: String,
    itemId: String? = null
): Request<DriveItemList>(
    path = if (itemId == null) "/drives/$driveId/root/children" else "/drives/$driveId/items/$itemId/children"
)
