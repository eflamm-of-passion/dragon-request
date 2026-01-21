package io.eflamm.dragonrequest.httprequestengine

import io.eflamm.dragonrequest.domain.Logger
import io.eflamm.dragonrequest.domain.RequestEngine
import io.eflamm.dragonrequest.domain.model.RequestInput
import io.eflamm.dragonrequest.domain.model.RequestResult
import io.eflamm.dragonrequest.httprequestengine.httpclient.HttpClient
import io.eflamm.dragonrequest.model.http.HttpRequest

class HttpRequestEngine(
    private val httpClient: HttpClient,
    private val logger: Logger
) : RequestEngine {
    override fun sendRequest(request: RequestInput): RequestResult {
        val httpRequest = request as HttpRequest
        logger.info { "request ${httpRequest.method} - ${httpRequest.url}" }
        logger.debug { "${httpRequest.body}" }
        val httpResponse = httpClient.sendRequest(httpRequest)
        logger.info { "response ${httpRequest.method} ${httpResponse.code} - ${httpRequest.url}" }
        logger.debug { "${httpResponse.body}" }
        return httpResponse
    }
}
