package io.eflamm.domain.repository

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.Id

interface EndpointRepository {
    fun getEndpoint(id: Id): Endpoint?
    fun createEndpoint(endpoint: Endpoint): Endpoint
    fun updateEndpoint(idOfEndpointToUpdate: Id, endpointUpdated: Endpoint): Endpoint
    fun deleteEndpoint(id: Id)
}