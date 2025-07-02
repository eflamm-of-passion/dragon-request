package io.eflamm.dragonrequest.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class WorkspaceCreateInput(
    @JsonProperty("name") val name: String,
)
