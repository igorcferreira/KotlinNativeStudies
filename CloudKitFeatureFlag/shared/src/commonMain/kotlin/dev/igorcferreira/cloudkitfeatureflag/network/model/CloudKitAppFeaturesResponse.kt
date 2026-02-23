package dev.igorcferreira.cloudkitfeatureflag.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CloudKitAppFeaturesResponse(
    val records: List<CloudKitAppFeatureRecord> = emptyList(),
)

@Serializable
data class CloudKitAppFeatureRecord(
    val recordName: String,
    val recordType: String,
    val fields: Map<String, CloudKitIntField> = emptyMap(),
    val recordChangeTag: String? = null,
    val created: CloudKitAuditInfo? = null,
    val modified: CloudKitAuditInfo? = null,
    val deleted: Boolean = false,
)

@Serializable
data class CloudKitIntField(
    val value: Long? = null,
    val type: String? = null,
) {
    val asBoolean: Boolean
        get() = value == 1L
}

@Serializable
data class CloudKitAuditInfo(
    val timestamp: Long,
    val userRecordName: String,
    val deviceID: String,
)
