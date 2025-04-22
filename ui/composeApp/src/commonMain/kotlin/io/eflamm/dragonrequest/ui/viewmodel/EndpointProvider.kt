package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint

interface EndpointProvider {
    suspend fun getAllEndpoints(): Result<List<Endpoint>>
    suspend fun createEndpoint(endpointToCreate: Endpoint): Result<Endpoint>
    suspend fun updateEndpoint(endpointToUpdate: Endpoint): Result<Endpoint>
    suspend fun deleteEndpoint(endpointToDelete: Endpoint): Result<Unit>
}