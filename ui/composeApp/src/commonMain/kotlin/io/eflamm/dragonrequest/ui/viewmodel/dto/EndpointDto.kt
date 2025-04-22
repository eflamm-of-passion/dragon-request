package io.eflamm.dragonrequest.ui.viewmodel.dto

import kotlinx.serialization.Serializable

@Serializable
data class EndpointDto(
    var id: String,
    var name: String,
    var httpMethod: String,
    var url: String,
)
