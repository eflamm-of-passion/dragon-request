package io.eflamm.dragonrequest.ui.model

import io.eflamm.dragonrequest.ui.model.states.ApiFileState

data class EndpointSnapshot(
    val state: ApiFileState,
    val name: String,
    val httpMethod: HttpMethod,
    val url: String,
)
