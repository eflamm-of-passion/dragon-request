package io.eflamm.dragonrequest.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty

// TODO enable one string constructor
data class EndpointUpdateInput (
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("httpMethod") val httpMethod: String,
    @JsonProperty("url") val url: String,
)