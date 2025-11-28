package dev.igorcferreira.sharepointembedded.file

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import dev.igorcferreira.msgraphapi.model.Folder
import dev.igorcferreira.msgraphapi.model.Identity
import dev.igorcferreira.msgraphapi.model.IdentitySet

@Composable
fun DriveItemCard(
    item: DriveItem,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val user by remember { derivedStateOf {
        item.createdBy.user?.displayName
            ?: item.createdBy.application?.displayName
            ?: ""
    } }

    Button(
        modifier = modifier,
        onClick = { onClick?.invoke() },
        shape = ShapeDefaults.Medium,
        contentPadding = ButtonDefaults.TextButtonContentPadding,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (item.isFolder) {
                    Icon(imageVector = Icons.Filled.Folder, contentDescription = "Folder")
                } else {
                    Icon(imageVector = Icons.Filled.FileOpen, contentDescription = "Folder")
                }
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            if (user.isNotBlank()) {
                Text(
                    text = user,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
@Preview("User file")
fun DriveItemCard_Preview() {
    MaterialTheme {
        DriveItemCard(
            item = DriveItem(
                id = "sample",
                name = "Sample Name",
                eTag = "",
                webUrl = "",
                createdBy = IdentitySet(
                    user = Identity(displayName = "Sample User")
                ),
                createdDateTime = ""
            )
        )
    }
}

@Composable
@Preview("Application folder")
fun DriveItemCard_PreviewFolder() {
    MaterialTheme {
        DriveItemCard(
            item = DriveItem(
                id = "sample",
                name = "Sample Name",
                eTag = "",
                webUrl = "",
                createdBy = IdentitySet(
                    application = Identity(displayName = "Sample Application")
                ),
                createdDateTime = "",
                folder = Folder(childCount = 0)
            )
        )
    }
}
