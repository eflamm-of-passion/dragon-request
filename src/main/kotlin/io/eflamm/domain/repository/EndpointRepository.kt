package io.eflamm.domain.repository

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.Id

interface EndpointRepository {
    fun getEndpoints(): Result<List<Endpoint>>
    fun getEndpoint(id: Id): Result<Endpoint>
    fun createEndpoint(endpoint: Endpoint): Result<Endpoint>
    fun updateEndpoint(endpointUpdated: Endpoint): Result<Endpoint>
    fun deleteEndpoint(id: Id): Result<Unit>
}
