package io.eflamm.infrastructure.api

import io.eflamm.application.mapper.EndpointMapper
import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.DeleteEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.application.usecase.UpdateEndpointUseCase
import io.eflamm.domain.monitoring.Logger
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
    private val deleteEndpointUseCase: DeleteEndpointUseCase,
    private val logger: Logger
) {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getEndpoint(@PathParam("id") id: String): Response {
        logger.info("request GET /${id}")
        val getEndpointResult = getEndpointUseCase.execute(id)
        return if(getEndpointResult.isSuccess) {
            logger.info("response GET /$id - 200 OK")
            Response.ok(EndpointMapper.businessToDto(getEndpointResult.getOrNull()!!)).build()
        } else {
            // TODO handle server error
            // TODO return error message
            logger.info("response GET /${id} - 404 not found")
            Response.status(404).build()
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createEndpoint(endpointInput: EndpointCreateInput): Response {
        logger.info("request POST / - $endpointInput")
        val createEndpointResult =  createEndpointUseCase.execute(EndpointMapper.dtoToBusiness(endpointInput))
        val createdEndpoint = createEndpointResult.getOrNull()
        if(createEndpointResult.isSuccess && createdEndpoint != null) {
                logger.info("response POST / - 201 created - $createdEndpoint")
                val locationUri = URI.create("/endpoints/${createdEndpoint.id}")
                return Response.created(locationUri).entity(EndpointMapper.businessToDto(createdEndpoint)).build()
        } else {
            // TODO return error message
            logger.info("response POST / - 500 server error")
            return Response.serverError().build()
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    fun updateEndpoint(endpointInput: EndpointUpdateInput): Response {
        logger.info("request PUT / - $endpointInput")
        val updateEndpointResult =  updateEndpointUseCase.execute(EndpointMapper.dtoToBusiness(endpointInput))
        val updatedEndpoint = updateEndpointResult.getOrNull()
        if(updateEndpointResult.isSuccess && updatedEndpoint != null) {
            logger.info("response PUT / - 200 OK - $updatedEndpoint")
            return Response.ok().entity(EndpointMapper.businessToDto(updatedEndpoint)).build()

        } else {
            // TODO return error message
            logger.info("response POST / - 500 server error")
            return Response.serverError().build()
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteEndpoint(@PathParam("id") id: String): Response {
        logger.info("request DELETE /${id}")
        val deleteEndpointResult = deleteEndpointUseCase.execute(id)
        return if(deleteEndpointResult.isSuccess) {
            logger.info("response DELETE /${id} - 200 OK")
            Response.ok().build()
        } else {
            // TODO handle server error
            logger.info("response DELETE /${id} - 404 not found")
            Response.status(404).build()
        }
    }
}