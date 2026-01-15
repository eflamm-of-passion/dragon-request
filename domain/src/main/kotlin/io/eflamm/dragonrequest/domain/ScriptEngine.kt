package io.eflamm.dragonrequest.domain

interface ScriptEngine {
    fun buildRequest(scriptName: String, inputForRequest: Any): Any
    fun validateResult(scriptName: String, requestResult: Any)
}