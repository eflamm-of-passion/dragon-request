package io.eflamm.dragonrequest.httprequestengine.httpclient

import io.eflamm.dragonrequest.model.http.HttpMethod
import io.eflamm.dragonrequest.model.http.HttpRequest
import io.eflamm.dragonrequest.model.http.HttpResponse
import java.net.URI
import java.net.http.HttpRequest.BodyPublishers
import java.time.Duration
import java.net.http.HttpClient as NetHttpClient
import java.net.http.HttpRequest as NetHttpRequest
import java.net.http.HttpResponse as NetHttpResponse

private const val JSON_CONTENT_TYPE = "application/json; charset=UTF-8"

class JavaNetHttpClient(
    private val httpClient: NetHttpClient,
) : HttpClient {
    override fun sendRequest(httpRequest: HttpRequest): HttpResponse {
        val requestBuilder =
            NetHttpRequest
                .newBuilder()
                .header("Content-Type", JSON_CONTENT_TYPE)
                .version(NetHttpClient.Version.HTTP_2)
                .timeout(Duration.ofSeconds(20))
                .uri(URI(httpRequest.url))

        val request =
            when (httpRequest.method) {
                HttpMethod.GET -> requestBuilder.GET().build()
                HttpMethod.POST -> requestBuilder.POST(BodyPublishers.ofString(httpRequest.body)).build()
                else -> throw IllegalArgumentException("Unsupported HTTP method: ${httpRequest.method}")
            }

        val response = httpClient.send<String>(request, NetHttpResponse.BodyHandlers.ofString())

        val headers: Map<String, String> =
            response
                .headers()
                .map()
                .mapValues { (_, values) -> values.joinToString(",") }

        return HttpResponse(
            code = response.statusCode(),
            headers = headers,
            body = response.body(),
        )
    }
}
