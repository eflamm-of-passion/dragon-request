package io.eflamm.dragonrequest.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CollectionOutput(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("children") val files: List<ApiFileOutput>,
) : ApiFileOutput
