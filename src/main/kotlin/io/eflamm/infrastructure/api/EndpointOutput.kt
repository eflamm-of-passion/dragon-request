package io.eflamm.infrastructure.api

data class EndpointOutput(
    val id: String?,
    val protocol: String,
    val domain: String,
    val port: Int,
    val path: String,
    val queryParameters: String
)
