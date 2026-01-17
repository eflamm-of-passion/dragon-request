package io.eflamm.dragonrequest.httprequestengine.httpclient

import io.eflamm.dragonrequest.model.http.HttpMethod
import io.eflamm.dragonrequest.model.http.HttpRequest
import io.eflamm.dragonrequest.model.http.HttpResponse
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.http.HttpHeaders
import java.util.function.BiPredicate
import java.net.http.HttpClient as NetHttpClient
import java.net.http.HttpRequest as NetHttpRequest
import java.net.http.HttpResponse as NetHttpResponse

@ExtendWith(MockKExtension::class)
class JavaNetHttpClientTest {
    private val mockedNetHttpClient: NetHttpClient = mockk()
    private val httpClient = JavaNetHttpClient(mockedNetHttpClient)

    @Test
    fun `send POST request then return a JSON response`() {
        // given
        val expectedCode = 200
        val expectedBody =
            """
            {
                "message": "(ノಠ益ಠ)ノ"
            }
            """.trimIndent()

        val mockedResponse: NetHttpResponse<String> = mockk()
        every { mockedResponse.statusCode() } returns expectedCode
        every { mockedResponse.headers() } returns
            HttpHeaders.of(
                mapOf("Content-Type" to listOf("application/json; charset=UTF-8")),
                BiPredicate { _, _ -> true },
            )
        every { mockedResponse.body() } returns expectedBody

        every {
            mockedNetHttpClient.send(any<NetHttpRequest>(), any<NetHttpResponse.BodyHandler<String>>())
        } returns mockedResponse

        val request =
            HttpRequest(
                method = HttpMethod.POST,
                url = "https://example.org",
                body =
                    """
                    {
                        "message": "Hello World !"
                    }
                    """.trimIndent(),
            )

        // when
        val actual: HttpResponse = httpClient.sendRequest(request)

        // then
        actual.code shouldBe expectedCode
        actual.headers shouldBe mapOf("Content-Type" to "application/json; charset=UTF-8")
        actual.body shouldBe expectedBody
    }
}
