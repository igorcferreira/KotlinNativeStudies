package dev.igorcferreira.msgraphapi.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OAuthToken(
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("ext_expires_in")
    val extExpiresIn: Int,
    @SerialName("access_token")
    val accessToken: String,
)
