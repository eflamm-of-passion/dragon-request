package io.eflamm.dragonrequest.model.http

data class HttpResponse(
    val code: Int,
    val headers: Map<String, String> = mutableMapOf(),
    val body: String?,
)
