package io.eflamm.dragonrequest.infrastructure.api

import com.fasterxml.jackson.annotation.JsonProperty

data class WorkspaceUpdateInput(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
)
