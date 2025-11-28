package dev.igorcferreira.msgraphapi.model

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val mimeType: String,
    val hashes: Hashes? = null,
) {
    @Serializable
    data class Hashes(
        val crc32Hash: String? = null,
        val quickXorHash: String? = null,
        val sha1Hash: String? = null,
    )
}
