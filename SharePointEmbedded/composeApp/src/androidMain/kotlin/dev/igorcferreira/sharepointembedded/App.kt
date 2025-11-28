package dev.igorcferreira.sharepointembedded

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.igorcferreira.sharepointembedded.file.DriveItemCard
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Composable
@Preview
fun App(
    modifier: Modifier = Modifier,
    selectedItem: String? = null,
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = koinViewModel(),
) {
    val activity = LocalActivity.current
    val currentItem by rememberSaveable {
        mutableStateOf(selectedItem ?: "root")
    }
    val loading by viewModel.loading.collectAsState()
    val username by viewModel.username.map {
        it ?: ""
    }.collectAsState(initial = "")
    val error by viewModel.error.map {
        it?.message ?: ""
    }.collectAsState(initial = "")

    LaunchedEffect(currentItem) {
        viewModel.load(itemId = selectedItem)
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
                item { Text("Empty Folder") }
            } else {
                items(list) { item ->
                    DriveItemCard(item) {
                        navController.navigate(DriveItemRoute.from(item))
                    }
                }
            }

            if (error.isNotBlank()) {
                item(error) { Text(error) }
            }
        }
    }
}
