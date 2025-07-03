package io.eflamm.dragonrequest.infrastructure.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

sealed interface ApiFileCreateInput {
    data class Workspace(
        @JsonProperty("name") val name: String,
    )

    data class Collection(
        @JsonProperty("name") val name: String,
        @JsonProperty("parentId") val parentId: String,
    )

    data class Endpoint(
        @JsonProperty("name") val name: String,
        @JsonProperty("httpMethod") val httpMethod: String,
        @JsonProperty("url") val url: String,
        @JsonProperty("parentId") val parentId: String,
    )
}
