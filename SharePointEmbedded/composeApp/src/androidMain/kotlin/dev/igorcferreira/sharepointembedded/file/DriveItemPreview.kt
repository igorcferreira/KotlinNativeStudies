package dev.igorcferreira.sharepointembedded.file

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.igorcferreira.msgraphapi.files.response.DriveItem
import dev.igorcferreira.sharepointembedded.ItemPreviewViewModel
import dev.igorcferreira.sharepointembedded.di.DIHelper
import dev.igorcferreira.sharepointembedded.rememberSerialSaveable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun DriveItemPreview(
    item: DriveItem,
    modifier: Modifier = Modifier,
    viewModel: ItemPreviewViewModel = koinViewModel(),
) {
    val loading by viewModel.loading.collectAsState()
    val page by viewModel.previewUrl.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentItem by rememberSerialSaveable { mutableStateOf(item) }

    LaunchedEffect(currentItem) {
        viewModel.load(itemId = currentItem.id)
    }

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.padding(16.dp))
        }
        if (!error.isNullOrBlank()) {
            Text(error!!, Modifier.padding(16.dp))
        }

        if (!page.isNullOrBlank()) {
            WebView(page!!)
        }
    }
}

@Composable
fun WebView(
    page: String,
    modifier: Modifier = Modifier,
    onPageLoaded: () -> Unit = {},
) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.supportZoom()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (page != url) { return }
                    onPageLoaded()
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return if (request?.url.toString() == page) {
                        super.shouldOverrideUrlLoading(view, request)
                    } else {
                        true
                    }
                }
            }
        }
    }, update = {
        it.loadUrl(page)
    }, modifier = modifier)
}

@Composable
@Preview
fun WebView_Preview() = KoinApplicationPreview(application = {
    modules(DIHelper.MODULES)
}) {
    MaterialTheme {
        WebView(
            page = "https://learn.microsoft.com/en-us/graph/sdks/sdks-overview?view=graph-rest-1.0",
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
