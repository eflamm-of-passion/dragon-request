package io.eflamm.dragonrequest.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Endpoint(
    val id: String,
    val httpMethod: String,
    val url: String,
    val hasChanges: Boolean = false
)