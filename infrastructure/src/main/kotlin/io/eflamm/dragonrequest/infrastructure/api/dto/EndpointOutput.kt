package io.eflamm.dragonrequest.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.eflamm.dragonrequest.infrastructure.api.dto.ApiFileOutput

data class EndpointOutput(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("httpMethod") val httpMethod: String,
    @JsonProperty("url") val url: String,
) : ApiFileOutput
