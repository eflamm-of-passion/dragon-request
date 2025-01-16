package io.eflamm.dragonrequest.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty

// TODO enable one string constructor
data class EndpointCreateInput (
    @JsonProperty("url") val url: String
)