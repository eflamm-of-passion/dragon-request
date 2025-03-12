package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json


class RestEndpointProvider(private val apiUrl: String)
    : EndpointProvider {
    companion object {
        const val BASE_PATH = "/endpoints"
    }
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun getAllEndpoints(): List<Endpoint> {
        val response: HttpResponse = client.get(apiUrl + BASE_PATH) {
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Origin, "http://localhost:3000")
        }

        if (response.status.value == 200) {
            return response.body()
        }
        return emptyList()
    }

    override suspend fun createEndpoint(endpointToCreate: Endpoint): Endpoint {
        val response: HttpResponse = client.post(apiUrl + BASE_PATH) {
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Origin, "http://localhost:3000")
            setBody(endpointToCreate)
        }
        if (response.status.value == 201) {
            return response.body()
        } else {
            throw Exception() // FIXME return result instead
        }
    }

    override suspend fun updateEndpoint(endpointToUpdate: Endpoint): Endpoint {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEndpoint(endpointToDelete: Endpoint) {
        client.delete(apiUrl + BASE_PATH + "/" + endpointToDelete.id) {
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Origin, "http://localhost:3000")
        }
    }
}