package dev.igorcferreira.cloudkitfeatureflag.domain.mapper

import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitAppFeatureRecord
import kotlin.time.Clock

internal class AppFeatureMapper: DomainMapper<CloudKitAppFeatureRecord, AppFeatures> {
    override fun map(
        networkModel: CloudKitAppFeatureRecord
    ): AppFeatures {
        val fields = networkModel.fields
        return AppFeatures(
            id = networkModel.modified?.timestamp ?: Clock.System.now().toEpochMilliseconds(),
            featureA = fields["FeatureA"]?.asBoolean ?: false,
            featureB = fields["FeatureB"]?.asBoolean ?: false,
        )
    }
}
