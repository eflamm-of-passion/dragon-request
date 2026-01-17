package io.eflamm.dragonrequest.domain

import io.eflamm.dragonrequest.domain.model.RequestInput

interface ScriptEngine {
    fun buildRequest(
        scriptName: String,
        inputForRequest: Any,
    ): RequestInput

    fun validateResult(
        scriptName: String,
        requestResult: Any,
    )
}
