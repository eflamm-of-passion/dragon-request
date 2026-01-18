package io.eflamm.dragonrequest.domain

import io.eflamm.dragonrequest.domain.model.EmptyRequestResult
import io.eflamm.dragonrequest.domain.model.RequestInput
import io.eflamm.dragonrequest.domain.model.RequestResult

class UseCases(
    private val requestEngine: RequestEngine,
    private val scriptEngine: ScriptEngine,
) {
    fun executeWorkflow(listScriptPath: List<String>) {
        listScriptPath.fold(Object() as Any) { previousResult, scriptName ->
            executeScript(scriptName, EmptyRequestResult)
        }
    }

    private fun executeScript(
        scriptPath: String,
        previousResult: RequestResult,
    ): Any {
        val request: RequestInput = scriptEngine.buildRequest(scriptPath, previousResult)
        val result: RequestResult = requestEngine.sendRequest(request)
        scriptEngine.validateResult(scriptPath, result)
        return result
    }
}
