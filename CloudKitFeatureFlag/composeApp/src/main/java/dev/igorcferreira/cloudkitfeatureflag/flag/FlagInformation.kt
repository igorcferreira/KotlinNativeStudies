package dev.igorcferreira.cloudkitfeatureflag.flag

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures

@Composable
fun FlagInformation(
    features: AppFeatures
) {
    Column {
        Text("Feature A: ${features.featureA}")
        Text("Feature B: ${features.featureB}")
    }
}

@Composable
@Preview(showBackground = true)
fun FlagInformationPreview() {
    FlagInformation(AppFeatures.empty)
}
