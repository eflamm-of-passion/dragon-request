package io.eflamm.infrastructure.api

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.DeleteEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.application.usecase.UpdateEndpointUseCase
import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import jakarta.ws.rs.*
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.net.URI

@Path("/endpoints")
class EndpointsController(
    private val getEndpointUseCase: GetEndpointUseCase,
    private val createEndpointUseCase: CreateEndpointUseCase,
    private val updateEndpointUseCase: UpdateEndpointUseCase,
    private val deleteEndpointUseCase: DeleteEndpointUseCase
) {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getEndpoint(@PathParam("id") id: String): Response {
        val getEndpointResult = getEndpointUseCase.execute(id)
        return if(getEndpointResult.isSuccess) {
            Response.ok(entityToDto(getEndpointResult.getOrNull()!!)).build()
        } else {
            // TODO return error message
            Response.status(404).build()
        }
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
            // TODO return error message
            return Response.serverError().build()
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    fun updateEndpoint(endpointInput: EndpointUpdateInput): Response {
        val updateEndpointResult =  updateEndpointUseCase.execute(dtoToEntity(endpointInput))
        val updatedEndpoint = updateEndpointResult.getOrNull()
        if(updateEndpointResult.isSuccess && updatedEndpoint != null) {
            return Response.ok().entity(entityToDto(updatedEndpoint)).build()

        } else {
            // TODO return error message
            return Response.serverError().build()
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteEndpoint(@PathParam("id") id: String): Response {
        val deleteEndpointResult = deleteEndpointUseCase.execute(id)
        return if(deleteEndpointResult.isSuccess)
            Response.ok().build()
        else
            Response.status(404).build()
    }

    private fun dtoToEntity(e: EndpointCreateInput): Endpoint {
        // TODO create a function in Endpoint to create
        return  Endpoint(
            id = Id.create(),
            protocol = Protocol.fromString(e.protocol), // TODO clean that
            domain = DomainName(e.domain),
            port = Port( e.port),
            path = io.eflamm.domain.model.endpoint.Path(emptyList()),
            queryParameters = QueryParameters(emptyMap()))
    }

    private fun dtoToEntity(e: EndpointUpdateInput): Endpoint {
        // TODO create a function in Endpoint to create
        return  Endpoint(
            id = Id.fromString(e.id),
            protocol = Protocol.fromString(e.protocol), // TODO clean that
            domain = DomainName(e.domain),
            port = Port( e.port),
            path = io.eflamm.domain.model.endpoint.Path(emptyList()),
            queryParameters = QueryParameters(emptyMap()))
    }

    private fun entityToDto(endpointEntity: Endpoint): EndpointOutput {
        // TODO clean that
        return EndpointOutput(
            endpointEntity.id.get(),
            endpointEntity.protocol.get(),
            endpointEntity.domain.get(),
            endpointEntity.port.get(),
            endpointEntity.path.aggregate(),
            endpointEntity.queryParameters.aggregate()
        )
    }
}