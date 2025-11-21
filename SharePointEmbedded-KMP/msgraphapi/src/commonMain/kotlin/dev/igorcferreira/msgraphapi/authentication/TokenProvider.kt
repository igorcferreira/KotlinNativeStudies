package dev.igorcferreira.msgraphapi.authentication

import dev.igorcferreira.msgraphapi.network.Network

internal interface TokenProvider {
    @Throws(Exception::class, Network.NetworkException::class)
    suspend fun getAccessToken(): String
    @Throws(Exception::class, Network.NetworkException::class)
    suspend fun signOut()
}
