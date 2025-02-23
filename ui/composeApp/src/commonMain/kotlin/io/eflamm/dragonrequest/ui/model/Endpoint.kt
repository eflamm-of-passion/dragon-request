package io.eflamm.dragonrequest.ui.model

data class Endpoint(
    val id: String,
    val httpMethod: String,
    val url: String,
    val hasChanges: Boolean
)