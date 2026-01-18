package io.eflamm.dragonrequest.domain

import io.eflamm.dragonrequest.domain.model.RequestInput
import io.eflamm.dragonrequest.domain.model.RequestResult

interface ScriptEngine {
    fun buildRequest(
        scriptName: String,
        inputForRequest: RequestResult,
    ): RequestInput

    fun validateResult(
        scriptName: String,
        requestResult: Any,
    )
}
