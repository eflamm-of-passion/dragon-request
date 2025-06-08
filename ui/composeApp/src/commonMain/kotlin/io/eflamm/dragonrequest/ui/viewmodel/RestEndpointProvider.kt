package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.HttpMethod
import io.eflamm.dragonrequest.ui.model.states.SavedRemoteUnedited
import io.eflamm.dragonrequest.ui.viewmodel.dto.EndpointDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json

class RestEndpointProvider(
    private val apiUrl: String,
) : EndpointProvider {
    companion object {
        const val BASE_PATH = "/endpoints"
    }

    private val client =
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }

    override suspend fun getAllEndpoints(): Result<List<Endpoint>> {
        val response: HttpResponse =
            client.get(apiUrl + BASE_PATH) {
                commonHeaders()
            }

        return if (response.status.value == 200) {
            val endpointsResponse: List<EndpointDto> = response.body()
            val endpoints = endpointsResponse.map { mapToModel(it) }
            Result.success(endpoints)
        } else {
            Result.failure(Exception("Something happened while fetching the endpoints"))
        }
    }

    override suspend fun createEndpoint(endpointToCreate: Endpoint): Result<Endpoint> {
        val response: HttpResponse =
            client.post(apiUrl + BASE_PATH) {
                commonHeaders()
                setBody(endpointToCreate)
            }
        return if (response.status.value == 201) {
            val endpointResponse: EndpointDto = response.body()
            val createdEndpoint = mapToModel(endpointResponse)
            Result.success(createdEndpoint)
        } else {
            Result.failure(Exception("Something happened while creating the endpoint"))
        }
    }

    override suspend fun updateEndpoint(endpointToUpdate: Endpoint): Result<Endpoint> {
        val response =
            client.put(apiUrl + BASE_PATH + "/" + endpointToUpdate.id) {
                commonHeaders()
            }
        return if (response.status.value == 200) {
            val endpointResponse: EndpointDto = response.body()
            val updatedEndpoint = mapToModel(endpointResponse)
            Result.success(updatedEndpoint)
        } else {
            Result.failure(Exception("Something happened while updating the endpoint"))
        }
    }

    override suspend fun deleteEndpoint(endpointToDelete: Endpoint): Result<Unit> {
        val response =
            client.delete(apiUrl + BASE_PATH + "/" + endpointToDelete.id) {
                commonHeaders()
            }
        return if (response.status.value == 200) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Something happened while deleting the endpoint"))
        }
    }

    private fun HttpRequestBuilder.commonHeaders() {
        header(HttpHeaders.ContentType, "application/json")
        header(HttpHeaders.Origin, "http://localhost:3000")
    }

    private fun mapToModel(endpointDto: EndpointDto): Endpoint =
        Endpoint(
            id = endpointDto.id,
            name = endpointDto.name,
            httpMethod = HttpMethod.valueOf(endpointDto.httpMethod), // TODO handle wrong parsing
            url = endpointDto.url,
            state = SavedRemoteUnedited(),
        )
}
