package io.eflamm.infrastructure.persistence

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import io.eflamm.domain.repository.EndpointRepository

class MockEndpointRepository : EndpointRepository {
    companion object {
        const val CREATED_ENDPOINT_UUID = "04cce78b-095c-4f07-af3c-8489d923b923"
    }

    override fun getEndpoint(id: Id): Endpoint {
        return Endpoint(
            id = Id.createFromString(id.get()),
            Protocol.HTTP,
            DomainName("acme.org"),
            Port(80),
            Path.create(),
            QueryParameters.create()
        )
    }

    override fun createEndpoint(endpoint: Endpoint): Endpoint {
        val createdEndpoint = Endpoint(
            id = Id.createFromString(CREATED_ENDPOINT_UUID),
            endpoint.protocol,
            endpoint.domain,
            endpoint.port,
            endpoint.path,
            endpoint.queryParameters
        )
        return createdEndpoint
    }

    override fun updateEndpoint(idOfEndpointToUpdate: Id, endpointUpdated: Endpoint): Endpoint {
        TODO("Not yet implemented")
    }

    override fun deleteEndpoint(id: Id) {
        TODO("Not yet implemented")
    }

}