package io.eflamm.adapters.api

// TODO enable one string constructor
data class EndpointInput(val protocol: String, val domain: String, val port: Int, val path: String, val queryParameters: String)
