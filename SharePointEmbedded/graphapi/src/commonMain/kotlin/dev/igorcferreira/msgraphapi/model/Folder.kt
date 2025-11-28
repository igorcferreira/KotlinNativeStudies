package dev.igorcferreira.msgraphapi.model

import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val childCount: Int,
    val view: View? = null
) {
    @Serializable
    data class View(
        val sortBy: String,
        val sortOrder: String,
        val viewType: String
    )
}
