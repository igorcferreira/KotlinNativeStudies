package dev.igorcferreira.msgraphapi.files

import dev.igorcferreira.msgraphapi.MSGraphAPI
import dev.igorcferreira.msgraphapi.files.request.ListDriveItemChildrenRequest
import dev.igorcferreira.msgraphapi.network.Network

@Throws(Exception::class, Network.NetworkException::class)
suspend fun MSGraphAPI.listChildren(
    driveId: String,
    itemId: String? = null
) = perform(ListDriveItemChildrenRequest(driveId, itemId)).value
