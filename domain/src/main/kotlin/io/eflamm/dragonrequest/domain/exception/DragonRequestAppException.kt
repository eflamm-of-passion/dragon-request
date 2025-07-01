package io.eflamm.dragonrequest.domain.exception

enum class ErrorType {
    INVALID_INPUT,
    ENTITY_NOT_FOUND,
    TECHNICAL_ERROR,
}

class DragonRequestAppException(
    val type: ErrorType,
    override val message: String,
    override val cause: Exception?,
) : RuntimeException() {
    constructor(type: ErrorType, message: String) : this(type, message, null)
}
