package io.eflamm.domain.exception

enum class ErrorType {
    INVALID_INPUT,
    ENTITY_NOT_FOUND,
    TECHNICAL_ERROR
}
class EndpointException(val type: ErrorType, override val message: String, override val cause: Exception?): RuntimeException() {
    constructor(type: ErrorType, message: String) : this(type, message, null)
}
