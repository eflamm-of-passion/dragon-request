package io.eflamm.dragonrequest.kotlinscriptengine

import io.eflamm.dragonrequest.domain.Logger
import io.eflamm.dragonrequest.domain.ScriptEngine
import io.eflamm.dragonrequest.domain.model.RequestInput
import io.eflamm.dragonrequest.domain.model.RequestResult
import io.eflamm.dragonrequest.kotlinscriptengine.loader.ScriptLoader
import io.eflamm.dragonrequest.model.http.HttpMethod
import io.eflamm.dragonrequest.model.http.HttpRequest
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.valueOrThrow
import kotlin.script.experimental.host.StringScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class KotlinScriptEngine(private val scriptLoader: ScriptLoader, private val logger: Logger) : ScriptEngine {

    object HttpRequestScript : ScriptCompilationConfiguration({
        jvm {
            defaultImports(
                HttpRequest::class,
                HttpMethod::class
            )
            dependenciesFromCurrentContext(wholeClasspath = true)
            // FIXME is it possible to pass only the required classes
        }
    })

    override fun buildRequest(
        scriptName: String,
        inputForRequest: RequestResult
    ): RequestInput {
        val sourceCode = scriptLoader.load(scriptName)

        val evalResult = BasicJvmScriptingHost().eval(
            StringScriptSource(sourceCode),
            HttpRequestScript,
            ScriptEvaluationConfiguration {}
        )
        val returnValue = evalResult.valueOrThrow().returnValue

        val httpRequest = when (returnValue) {
            is ResultValue.Value -> returnValue.value as HttpRequest
            else -> throw IllegalStateException("Script did not return a value")
        }

        return httpRequest
    }

    override fun validateResult(scriptName: String, requestResult: Any) {
        TODO("Not yet implemented")
    }
}