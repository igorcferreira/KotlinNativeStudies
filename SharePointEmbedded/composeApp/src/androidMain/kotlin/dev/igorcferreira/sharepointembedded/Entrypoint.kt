package dev.igorcferreira.sharepointembedded

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import dev.igorcferreira.sharepointembedded.file.DriveItemPreview
import kotlinx.serialization.Serializable

@Serializable
object HomePage

@Serializable
sealed class DriveItemRoute {
    @Serializable
    data class FileRoute(
        val name: String,
        val fileId: String
    ) : DriveItemRoute()
    @Serializable
    data class FolderRoute(
        val name: String,
        val folderId: String
    ) : DriveItemRoute()

    companion object {
        fun from(item: DriveItem): DriveItemRoute = if (item.isFolder) {
            FolderRoute(item.name, item.id)
        } else {
            FileRoute(item.name, item.id)
        }
    }
}

@Suppress("AssignedValueIsNeverRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Entrypoint() {
    MaterialTheme {
        val navController = rememberNavController()
        var title by remember { mutableStateOf<String?>(null) }

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(title ?: "SharePoint", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {
                    if (title != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, tint = MaterialTheme.colorScheme.onPrimaryContainer, contentDescription = "Back")
                        }
                    }
                }
            )
        }) {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(it)) {
                NavHost(navController, startDestination = HomePage) {
                    composable<DriveItemRoute.FolderRoute> { entry ->
                        val route = entry.toRoute<DriveItemRoute.FolderRoute>()
                        title = route.name
                        App(selectedItem = route.folderId, navController = navController)
                    }
                    composable<DriveItemRoute.FileRoute> { entry ->
                        val route = entry.toRoute<DriveItemRoute.FileRoute>()
                        title = route.name
                        DriveItemPreview(itemId = route.fileId)
                    }
                    composable<HomePage> {
                        title = null
                        App(navController = navController)
                    }
                }
            }
        }
    }
}
