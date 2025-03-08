package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders


class RestEndpointProvider: EndpointProvider {

    override suspend fun getAllEndpoints(): List<Endpoint> {
        val client = HttpClient()
        val response: HttpResponse = client.get("http://localhost:8080/endpoints") {
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Origin, "http://localhost:3000")
        }
        println(response.bodyAsText())

        if (response.status.value in 200..299) {
            return response.body()
        }
        return emptyList()
    }
}