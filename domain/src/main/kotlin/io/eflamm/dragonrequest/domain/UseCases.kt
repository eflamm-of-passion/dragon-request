package io.eflamm.dragonrequest.domain

class UseCases(private val requestEngine: RequestEngine, private val scriptEngine: ScriptEngine) {
    fun executeWorkflow(listScriptNames: List<String>) {
        listScriptNames.fold(Object() as Any) { previousResult, scriptName ->
            executeScript(scriptName, previousResult)
        }
    }

    fun executeScript(scriptName: String, previousResult: Any): Any {
        val request: Any = scriptEngine.buildRequest(scriptName, previousResult)
        val result: Any = requestEngine.sendRequest(request)
        scriptEngine.validateResult(scriptName, result)
        return result
    }
}