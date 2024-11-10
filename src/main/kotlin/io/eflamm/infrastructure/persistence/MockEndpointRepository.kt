package io.eflamm.infrastructure.persistence

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import io.eflamm.domain.repository.EndpointRepository

class MockEndpointRepository: EndpointRepository {
    companion object {
        const val CREATED_ENDPOINT_UUID = "04cce78b-095c-4f07-af3c-8489d923b923"
    }

    override fun getEndpoints(): Result<List<Endpoint>> {
        TODO("Not yet implemented")
    }

    override fun getEndpoint(id: Id): Result<Endpoint> {
        return Result.success(Endpoint(
            id = Id.fromString(id.get()),
            Protocol.HTTP,
            DomainName("acme.org"),
            Port(80),
            Path.create(),
            QueryParameters.create()
        ))
    }

    override fun createEndpoint(endpoint: Endpoint): Result<Endpoint> {
        val createdEndpoint = Endpoint(
            id = Id.fromString(CREATED_ENDPOINT_UUID),
            endpoint.protocol,
            endpoint.domain,
            endpoint.port,
            endpoint.path,
            endpoint.queryParameters
        )
        return Result.success(createdEndpoint)
    }

    override fun updateEndpoint(endpointUpdated: Endpoint): Result<Endpoint> {
        TODO("Not yet implemented")
    }

    override fun deleteEndpoint(id: Id): Result<Unit> {
        TODO("Not yet implemented")
    }

}
