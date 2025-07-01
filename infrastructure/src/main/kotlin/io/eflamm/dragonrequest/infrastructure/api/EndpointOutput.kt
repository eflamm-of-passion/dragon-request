package io.eflamm.dragonrequest.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty

data class EndpointOutput(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("httpMethod") val httpMethod: String,
    @JsonProperty("url") val url: String,
) : ApiFileOutput
