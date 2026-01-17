package io.eflamm.dragonrequest.httprequestengine

import io.eflamm.dragonrequest.httprequestengine.httpclient.HttpClient
import io.eflamm.dragonrequest.model.http.HttpMethod
import io.eflamm.dragonrequest.model.http.HttpRequest
import io.eflamm.dragonrequest.model.http.HttpResponse
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class HttpRequestEngineTest {
    @Test
    fun `WHEN send a request THEN return a successful result`() {
        // given
        val httpRequest = HttpRequest(
            method = HttpMethod.POST,
            url = "https://example.org",
            body = """
                    {
                        "message": "Hello World !"
                    }
                    """.trimIndent()
        )
        val mockedHttpClient: HttpClient = mockk()
        every { mockedHttpClient.sendRequest(httpRequest) } returns HttpResponse(
            code = 200,
            headers = mapOf("Content-Type" to "application/json; charset=UTF-8"),
            body = """
            {
                "message": "(ノಠ益ಠ)ノ"
            }
            """.trimIndent()
        )

        // when
        val actual = HttpRequestEngine(mockedHttpClient).sendRequest(httpRequest) as HttpResponse

        // then
        actual shouldNotBeNull {}

    }
}
