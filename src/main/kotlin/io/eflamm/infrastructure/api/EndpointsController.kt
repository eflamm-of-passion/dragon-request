package io.eflamm.infrastructure.api

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.DomainName
import io.eflamm.domain.model.endpoint.Port
import io.eflamm.domain.model.endpoint.Protocol
import io.eflamm.domain.model.endpoint.QueryParameters
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.net.URI

@Path("/endpoints")
class EndpointsController(private val getEndpointUseCase: GetEndpointUseCase, private val createEndpointUseCase: CreateEndpointUseCase) {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getEndpoint(@PathParam("id") id: String): Response {
        val endpoint = getEndpointUseCase.execute(id)
        return Response.ok(entityToDto(endpoint)).build()
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createEndpoint(endpointInput: EndpointInput): Response {
        val createdEndpoint =  createEndpointUseCase.execute(dtoToEntity(endpointInput))
        val locationUri = URI.create("/endpoints/${createdEndpoint.id}")
        return Response.created(locationUri).entity(entityToDto(createdEndpoint)).build()
    }

    private fun dtoToEntity(e: EndpointInput): Endpoint {
        // TODO create a function in Endpoint to create
        return  Endpoint(
            protocol = Protocol.fromString(e.protocol) ?: Protocol.HTTP, // TODO clean that
            domain = DomainName(e.domain),
            port = Port( e.port),
            path = io.eflamm.domain.model.endpoint.Path(emptyList()),
            queryParameters = QueryParameters(emptyMap()))
    }

    private fun entityToDto(endpointEntity: Endpoint): EndpointOutput {
        // TODO clean that
        return EndpointOutput(
            endpointEntity.id?.get(),
            endpointEntity.protocol.get(),
            endpointEntity.domain.get(),
            endpointEntity.port.get(),
            endpointEntity.path.aggregate(),
            endpointEntity.queryParameters.aggregate()
        )
    }
}