package io.eflamm.infrastructure.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

// TODO enable one string constructor
data class EndpointUpdateInput @JsonCreator constructor(
    @JsonProperty("id") val id: String,
    @JsonProperty("url") val url: String,
)