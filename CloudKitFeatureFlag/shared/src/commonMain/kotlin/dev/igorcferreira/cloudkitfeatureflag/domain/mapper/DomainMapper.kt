package dev.igorcferreira.cloudkitfeatureflag.domain.mapper

interface DomainMapper<NetworkModel, DomainModel> {
    fun map(networkModel: NetworkModel): DomainModel
}
