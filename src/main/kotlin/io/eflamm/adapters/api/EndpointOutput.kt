package io.eflamm.adapters.api

data class EndpointOutput(val identifier: String?, val protocol: String, val domain: String, val port: String, val path: String, val queryParameters: String)
