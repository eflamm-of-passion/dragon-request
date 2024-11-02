package io.eflamm.infrastructure.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

// TODO enable one string constructor
data class EndpointCreateInput @JsonCreator constructor(
    @JsonProperty("protocol") val protocol: String,
    @JsonProperty("domain") val domain: String,
    @JsonProperty("port") val port: Int,
    @JsonProperty("path") val path: String,
    @JsonProperty("queryParameters") val queryParameters: String
)