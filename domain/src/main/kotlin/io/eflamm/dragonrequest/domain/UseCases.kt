package io.eflamm.dragonrequest.domain

import io.eflamm.dragonrequest.domain.model.RequestInput
import io.eflamm.dragonrequest.domain.model.RequestResult

class UseCases(
    private val requestEngine: RequestEngine,
    private val scriptEngine: ScriptEngine,
) {
    fun executeWorkflow(listScriptNames: List<String>) {
        listScriptNames.fold(Object() as Any) { previousResult, scriptName ->
            executeScript(scriptName, previousResult)
        }
    }

    fun executeScript(
        scriptName: String,
        previousResult: Any,
    ): Any {
        val request: RequestInput = scriptEngine.buildRequest(scriptName, previousResult)
        val result: RequestResult = requestEngine.sendRequest(request)
        scriptEngine.validateResult(scriptName, result)
        return result
    }
}
