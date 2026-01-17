package io.eflamm.dragonrequest.model.http

import io.eflamm.dragonrequest.domain.model.RequestResult

data class HttpResponse(
    val code: Int,
    val headers: Map<String, String> = mutableMapOf(),
    val body: String?,
): RequestResult
