package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint

interface EndpointProvider {
    suspend fun getAllEndpoints(): List<Endpoint>
    suspend fun createEndpoint(endpointToCreate: Endpoint): Endpoint
    suspend fun updateEndpoint(endpointToUpdate: Endpoint): Endpoint
    suspend fun deleteEndpoint(endpointToDelete: Endpoint)
}