package io.eflamm.infrastructure.persistence

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import io.eflamm.domain.repository.EndpointRepository

class MockEndpointRepository : EndpointRepository {
    override fun getEndpoint(id: Id): Endpoint {
        return Endpoint(id = Id.createFromString(id.get()), Protocol.HTTP, DomainName("acme.org"),  Port(80), Path.create(), QueryParameters.create())
    }

    override fun createEndpoint(endpoint: Endpoint): Endpoint {
        TODO("Not yet implemented")
    }

    override fun updateEndpoint(idOfEndpointToUpdate: Id, endpointUpdated: Endpoint): Endpoint {
        TODO("Not yet implemented")
    }

    override fun deleteEndpoint(id: Id) {
        TODO("Not yet implemented")
    }

}