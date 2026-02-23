package dev.igorcferreira.cloudkitfeatureflag.domain.repository

import dev.igorcferreira.cloudkitfeatureflag.domain.mapper.DomainMapper
import dev.igorcferreira.cloudkitfeatureflag.model.AppFeatures
import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitIntField
import dev.igorcferreira.cloudkitfeatureflag.network.repository.CloudKitFeatureRepository

class AppFeatureRepository(
    private val container: String,
    private val recordName: String,
    private val cloudKitFeatureRepository: CloudKitFeatureRepository,
    private val mapper: DomainMapper<Map<String, CloudKitIntField>, AppFeatures>
) {

    suspend fun getAppFeatures(): AppFeatures {
        val networkModel = cloudKitFeatureRepository.getFeatures(
            container = container,
            recordName = recordName
        )
        return mapper.map(networkModel)
    }
}
