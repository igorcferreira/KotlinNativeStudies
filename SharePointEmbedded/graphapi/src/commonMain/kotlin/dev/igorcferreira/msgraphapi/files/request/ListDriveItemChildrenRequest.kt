package dev.igorcferreira.msgraphapi.files.request

import dev.igorcferreira.msgraphapi.files.response.DriveItemList
import dev.igorcferreira.msgraphapi.network.Request

class ListDriveItemChildrenRequest(
    driveId: String,
    itemId: String? = null
): Request<DriveItemList>(
    path = if (itemId.isNullOrEmpty() || itemId == "root")
        "/drives/$driveId/root/children"
    else
        "/drives/$driveId/items/$itemId/children"
)
