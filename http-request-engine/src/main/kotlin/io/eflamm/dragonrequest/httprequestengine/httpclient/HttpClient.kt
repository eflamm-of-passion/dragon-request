package io.eflamm.dragonrequest.httprequestengine.httpclient

import io.eflamm.dragonrequest.model.http.HttpRequest
import io.eflamm.dragonrequest.model.http.HttpResponse

interface HttpClient {
    fun sendRequest(httpRequest: HttpRequest): HttpResponse
}
