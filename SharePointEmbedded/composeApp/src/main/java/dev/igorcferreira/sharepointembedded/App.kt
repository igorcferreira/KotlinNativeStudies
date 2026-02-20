package dev.igorcferreira.sharepointembedded

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import dev.igorcferreira.sharepointembedded.di.DIHelper
import dev.igorcferreira.sharepointembedded.file.DriveItemCard
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun App(
    modifier: Modifier = Modifier,
    selectedItem: DriveItem? = null,
    navCommand: (DriveItem) -> Unit = {},
    viewModel: AppViewModel = koinViewModel(),
) {
    val activity = LocalActivity.current
    val loading by viewModel.loading.collectAsState()
    val username by viewModel.username.map {
        it ?: ""
    }.collectAsState(initial = "")
    val error by viewModel.error.map {
        it?.message ?: ""
    }.collectAsState(initial = "")

    val currentItem by rememberOptionalSerialSaveable {
        mutableStateOf(selectedItem)
    }

    LaunchedEffect(currentItem) {
        viewModel.load(item = currentItem)
        if (activity == null) return@LaunchedEffect

        viewModel.authenticate {
            attachedToActivity(activity)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(loading) {
            CircularProgressIndicator()
        }

        val list by viewModel.list.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (username.isNotBlank()) {
                item(username) { Text(username) }
            }

            if (list.isEmpty() && !loading) {
                item { Text(stringResource(R.string.empty_folder)) }
            } else {
                items(list) { item ->
                    DriveItemCard(item) {
                        navCommand(item)
                    }
                }
            }

            if (error.isNotBlank()) {
                item(error) { Text(error) }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun App_Preview() = KoinApplicationPreview(application = {
    modules(DIHelper.MODULES)
}) { Scaffold { padding ->
    App(modifier = Modifier.padding(padding))
}}
