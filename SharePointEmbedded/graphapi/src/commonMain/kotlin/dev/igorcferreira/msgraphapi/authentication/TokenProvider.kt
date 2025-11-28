package dev.igorcferreira.msgraphapi.authentication

import dev.igorcferreira.msgraphapi.network.NetworkException
import kotlinx.serialization.Serializable

interface TokenProvider {
    @Throws(NetworkException::class, AuthenticationError::class , Exception::class)
    suspend fun getToken(): String

    @Throws(NetworkException::class, AuthenticationError::class, Exception::class)
    suspend fun signOut()
}

interface UserProvider {
    @Throws(NetworkException::class, AuthenticationError::class, Exception::class)
    suspend fun getUserName(): String?
}

@Serializable
class AuthenticationError(
    override val message: String = "Unable to authenticate user",
): RuntimeException(message)

expect class MSAuthenticationProvider(
    tenantId: String,
    clientId: String,
    scopes: List<String>
): TokenProvider, UserProvider {
    override suspend fun getToken(): String
    override suspend fun signOut()
    override suspend fun getUserName(): String?
}
