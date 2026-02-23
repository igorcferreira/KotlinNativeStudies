package dev.igorcferreira.cloudkitfeatureflag.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.igorcferreira.cloudkitfeatureflag.Greeting
import dev.igorcferreira.cloudkitfeatureflag.R
import dev.igorcferreira.cloudkitfeatureflag.flag.FlagInformation
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures

@Composable
fun App(
    viewModel: AppViewModel = viewModel()
) {
    val features by viewModel.state.collectAsStateWithLifecycle()
    AppContent(features = features)
}

@Composable
fun AppContent(
    features: AppFeatures
) {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(R.drawable.ic_launcher_background), null)
                    Text("Compose: $greeting")
                    FlagInformation(features)
                }
            }
        }
    }
}

@Composable
@Preview
fun App_Preview() {
    AppContent(AppFeatures.empty)
}
