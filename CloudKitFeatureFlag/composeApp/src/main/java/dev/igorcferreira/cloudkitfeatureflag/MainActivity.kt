package dev.igorcferreira.cloudkitfeatureflag

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.igorcferreira.cloudkitfeatureflag.app.App
import dev.igorcferreira.cloudkitfeatureflag.app.AppContent
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppContent(AppFeatures.empty)
}
