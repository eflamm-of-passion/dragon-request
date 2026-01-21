package io.eflamm.dragonrequest

import io.eflamm.dragonrequest.domain.RequestEngine
import io.eflamm.dragonrequest.domain.ScriptEngine
import io.eflamm.dragonrequest.domain.UseCases
import io.eflamm.dragonrequest.domain.slf4jlogger.LoggerFactory
import io.eflamm.dragonrequest.httprequestengine.HttpRequestEngine
import io.eflamm.dragonrequest.httprequestengine.httpclient.JavaNetHttpClient
import io.eflamm.dragonrequest.kotlinscriptengine.KotlinScriptEngine
import io.eflamm.dragonrequest.kotlinscriptengine.loader.ResourceScriptLoader
import java.net.http.HttpClient

fun main(args: Array<String>) {
    val useCases = UseCases(requestEngine(), scriptEngine())
    useCases.executeWorkflow(listOf("scripts/first-post-request.kts")) // TODO give a script
}

private fun scriptEngine(): ScriptEngine =
    KotlinScriptEngine(ResourceScriptLoader(), LoggerFactory.forClass(KotlinScriptEngine::class.java))

private fun requestEngine(): RequestEngine = HttpRequestEngine(
    JavaNetHttpClient(HttpClient.newBuilder().build()),
    LoggerFactory.forClass(HttpRequestEngine::class.java)
)

