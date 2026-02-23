package dev.igorcferreira.cloudkitfeatureflag.model

import kotlinx.serialization.Serializable

@Serializable
data class AppFeatures(
    val featureA: Boolean = true,
    val featureB: Boolean = false,
) {
    companion object {
        val empty: AppFeatures
            get() = AppFeatures()
    }
}
