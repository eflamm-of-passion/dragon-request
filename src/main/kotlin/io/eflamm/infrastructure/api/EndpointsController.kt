package io.eflamm.infrastructure.api

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import jakarta.ws.rs.*
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.net.URI

@Path("/endpoints")
class EndpointsController(private val getEndpointUseCase: GetEndpointUseCase, private val createEndpointUseCase: CreateEndpointUseCase) {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getEndpoint(@PathParam("id") id: String): Response {
        val getEndpointResult = getEndpointUseCase.execute(id)
        return if(getEndpointResult.isSuccess)
            Response.ok(entityToDto(getEndpointResult.getOrNull()!!)).build()
        else
            Response.status(404).build()
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createEndpoint(endpointInput: EndpointCreateInput): Response {
        val createEndpointResult =  createEndpointUseCase.execute(dtoToEntity(endpointInput))
        val createdEndpoint = createEndpointResult.getOrNull()
        if(createEndpointResult.isSuccess && createdEndpoint != null) {
                val locationUri = URI.create("/endpoints/${createdEndpoint.id}")
                return Response.created(locationUri).entity(entityToDto(createdEndpoint)).build()
        } else {
            return Response.serverError().build()
        }
    }

    private fun dtoToEntity(e: EndpointCreateInput): Endpoint {
        // TODO create a function in Endpoint to create
        return  Endpoint(
            id = Id.create(),
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