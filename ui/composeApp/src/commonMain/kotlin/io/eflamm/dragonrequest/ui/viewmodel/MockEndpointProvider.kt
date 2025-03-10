package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint

class MockEndpointProvider: EndpointProvider {
    override suspend fun getAllEndpoints(): List<Endpoint> = listOf(
            Endpoint("1", "GET", "https://www.google.com", false),
            Endpoint("2", "POST", "https://www.youtube.com", false)
    )

    override suspend fun createEndpoint(endpointToCreate: Endpoint): Endpoint {
        TODO("Not yet implemented")
    }

    override suspend fun updateEndpoint(endpointToUpdate: Endpoint): Endpoint {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEndpoint(endpointToDelete: Endpoint) {
        TODO("Not yet implemented")
    }
}