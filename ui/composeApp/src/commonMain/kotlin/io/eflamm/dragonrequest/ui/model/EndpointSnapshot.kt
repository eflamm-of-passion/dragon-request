package io.eflamm.dragonrequest.ui.model

import kotlinx.serialization.Serializable

data class EndpointSnapshot(
    val state: EndpointState,
    val name: String,
    val httpMethod: HttpMethod,
    val url: String,
)
