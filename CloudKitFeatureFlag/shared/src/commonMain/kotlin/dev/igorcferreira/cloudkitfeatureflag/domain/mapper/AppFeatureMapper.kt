package dev.igorcferreira.cloudkitfeatureflag.domain.mapper

import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitIntField

class AppFeatureMapper: DomainMapper<Map<String, CloudKitIntField>, AppFeatures> {
    override fun map(
        networkModel: Map<String, CloudKitIntField>
    ): AppFeatures = AppFeatures(
        featureA = networkModel["FeatureA"]?.asBoolean ?: false,
        featureB = networkModel["FeatureB"]?.asBoolean ?: false,
    )
}
