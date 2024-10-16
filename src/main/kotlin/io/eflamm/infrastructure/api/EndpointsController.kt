package io.eflamm.infrastructure.api

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.DomainName
import io.eflamm.domain.model.endpoint.Port
import io.eflamm.domain.model.endpoint.Protocol
import io.eflamm.domain.model.endpoint.QueryParameters
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/endpoints")
class EndpointsController() {
//    class EndpointsController(private val getEndpointUseCase: GetEndpointUseCase, private val createEndpointUseCase: CreateEndpointUseCase) {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        return "Hello from Quarkus REST!!!!!!!!!!"
    }

//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    fun getEndpoint(@PathParam("id") id: String): EndpointOutput {
//        val endpoint = getEndpointUseCase.execute(id)
//        return entityToDto(endpoint)
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    fun createEndpoint(endpointInput: EndpointInput): EndpointOutput {
//        val endpoint =  createEndpointUseCase.execute(dtoToEntity(endpointInput))
//        return entityToDto(endpoint)
//    }

    private fun dtoToEntity(e: EndpointInput): Endpoint {
        // TODO create a function in Endpoint to create
        return  Endpoint(
            protocol = Protocol.valueOf(e.protocol),
            domain = DomainName(e.domain),
            port = Port( e.port),
            path = io.eflamm.domain.model.endpoint.Path(emptyList()),
            queryParameters = QueryParameters(emptyMap()))
    }

    private fun entityToDto(endpointEntity: Endpoint): EndpointOutput {
        // TODO clean that
        return EndpointOutput(endpointEntity.id?.get(), endpointEntity.protocol.get(), endpointEntity.domain.get(), endpointEntity.port.get(), endpointEntity.path.aggregate(), endpointEntity.queryParameters.aggregate())
    }
}