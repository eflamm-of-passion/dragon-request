package io.eflamm.dragonrequest.model.http

import io.eflamm.dragonrequest.domain.model.RequestInput

data class HttpRequest(
    val method: HttpMethod,
    val url: String,
    val body: String?,
): RequestInput

enum class HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTION,
    HEAD,
}
