package io.eflamm.dragonrequest.model.http

data class HttpRequest(
    val method: HttpMethod,
    val url: String,
    val body: String?,
)

enum class HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTION,
    HEAD,
}
