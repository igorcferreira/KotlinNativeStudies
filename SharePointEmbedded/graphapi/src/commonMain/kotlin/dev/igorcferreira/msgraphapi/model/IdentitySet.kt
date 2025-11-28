package dev.igorcferreira.msgraphapi.model

import kotlinx.serialization.Serializable

@Serializable
data class IdentitySet(
    val application: Identity? = null,
    val applicationInstance: Identity? = null,
    val conversation: Identity? = null,
    val conversationIdentityType: Identity? = null,
    val device: Identity? = null,
    val encrypted: Identity? = null,
    val onPremises: Identity? = null,
    val guest: Identity? = null,
    val phone: Identity? = null,
    val user: Identity? = null
)
