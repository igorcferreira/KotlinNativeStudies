package dev.igorcferreira.cloudkitfeatureflag.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class AppFeatures(
    val id: Long,
    val featureA: Boolean = false,
    val featureB: Boolean = false,
) {
    override fun hashCode(): Int = id.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val feature = other as? AppFeatures ?: return false
        return id == feature.id
    }

    companion object {
        val empty: AppFeatures
            get() = AppFeatures(id = Clock.System.now().toEpochMilliseconds())
    }
}
