package io.eflamm.dragonrequest.kotlinscriptengine

import io.eflamm.dragonrequest.domain.model.EmptyRequestResult
import io.eflamm.dragonrequest.kotlinscriptengine.loader.ResourceScriptLoader
import io.eflamm.dragonrequest.model.http.HttpMethod
import io.eflamm.dragonrequest.model.http.HttpRequest
import io.kotest.matchers.shouldBe
import kotlin.script.experimental.api.SourceCode
import org.junit.jupiter.api.Test
import stub.StubLogger

class KotlinScriptEngineTest {
    @Test
    fun `should read a request script without input data then build the request`() {
        // given
        val scriptPath = "scripts/first-post-request.kts"
        val expectedHttpRequest = HttpRequest(
            method = HttpMethod.POST,
            url = "https://example.org",
            body = """
                {
                    "message": "Hello world !"
                }
            """.trimIndent()
        )
        val resourceScriptLoader = ResourceScriptLoader()

        // when
        val actual = KotlinScriptEngine(resourceScriptLoader, StubLogger).buildRequest(scriptPath, EmptyRequestResult)

        // then
        actual shouldBe expectedHttpRequest
    }

}