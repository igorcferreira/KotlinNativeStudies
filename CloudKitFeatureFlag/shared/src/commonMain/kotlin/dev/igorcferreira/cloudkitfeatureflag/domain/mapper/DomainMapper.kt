package dev.igorcferreira.cloudkitfeatureflag.domain.mapper

internal interface DomainMapper<NetworkModel, DomainModel> {
    fun map(networkModel: NetworkModel): DomainModel
}
