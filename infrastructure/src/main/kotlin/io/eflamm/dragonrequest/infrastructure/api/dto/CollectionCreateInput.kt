package io.eflamm.dragonrequest.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CollectionCreateInput(
    @JsonProperty("name") val name: String,
    @JsonProperty("parentId") val parentId: String,
)
