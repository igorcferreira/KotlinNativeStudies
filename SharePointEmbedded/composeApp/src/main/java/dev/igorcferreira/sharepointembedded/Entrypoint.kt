package dev.igorcferreira.sharepointembedded

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import dev.igorcferreira.sharepointembedded.di.DIHelper
import dev.igorcferreira.sharepointembedded.file.DriveItemPreview
import kotlinx.serialization.Serializable
import org.koin.compose.KoinApplicationPreview

@Serializable
object HomePage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Entrypoint() {
    MaterialTheme {
        val backstack = remember { mutableStateListOf<Any>(HomePage) }
        val title = remember { derivedStateOf {
            val key = backstack.lastOrNull()
            (key as? DriveItem)?.name
        }}

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(title.value ?: stringResource(R.string.app_name), color = MaterialTheme.colorScheme.onPrimaryContainer) },
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {
                    if (title.value != null) {
                        IconButton(onClick = { backstack.removeLastOrNull() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, tint = MaterialTheme.colorScheme.onPrimaryContainer, contentDescription = "Back")
                        }
                    }
                }
            )
        }) { paddingValues ->
            NavDisplay(
                modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(paddingValues),
                backStack = backstack,
                onBack = { backstack.removeLastOrNull() },
                entryProvider = entryProvider {
                    homePageEntry(backstack)
                    driveItemEntry(backstack)
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Entrypoint_Preview() = KoinApplicationPreview(application = {
    modules(DIHelper.MODULES)
}) {
    Entrypoint()
}

private fun EntryProviderScope<Any>.homePageEntry(backstack: SnapshotStateList<Any>) {
    entry<HomePage> {
        App(navCommand = { item ->
            backstack.add(item)
        })
    }
}

private fun EntryProviderScope<Any>.driveItemEntry(backstack: SnapshotStateList<Any>) {
    entry<DriveItem> { key ->
        if (key.isFolder) {
            App(selectedItem = key, navCommand = { item ->
                backstack.add(item)
            })
        } else {
            DriveItemPreview(item = key)
        }
    }
}
