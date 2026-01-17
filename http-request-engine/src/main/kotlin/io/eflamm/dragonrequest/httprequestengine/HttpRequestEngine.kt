package io.eflamm.dragonrequest.httprequestengine

import io.eflamm.dragonrequest.domain.RequestEngine
import io.eflamm.dragonrequest.domain.model.RequestInput
import io.eflamm.dragonrequest.domain.model.RequestResult
import io.eflamm.dragonrequest.httprequestengine.httpclient.HttpClient

class HttpRequestEngine(
    private val httpClient: HttpClient,
) : RequestEngine {
    override fun sendRequest(request: RequestInput): RequestResult {
        // TODO cast RequestInput to HttpRequest
        // TODO use the HttpRequest to send a request with
        TODO("return the result")
    }
}
