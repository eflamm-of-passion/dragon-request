package io.eflamm.dragonrequest.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty

data class EndpointOutput(
    @JsonProperty("id") val id: String,
    @JsonProperty("url") val url: String
)
