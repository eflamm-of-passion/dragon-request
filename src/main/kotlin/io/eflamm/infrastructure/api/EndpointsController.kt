package io.eflamm.infrastructure.api

import io.eflamm.application.mapper.EndpointMapper
import io.eflamm.application.mapper.LoggerUtils
import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.DeleteEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.application.usecase.UpdateEndpointUseCase
import io.eflamm.domain.exception.EndpointException
import io.eflamm.domain.exception.ErrorType
import io.eflamm.domain.monitoring.Logger
import io.eflamm.domain.model.Endpoint
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
    object Constants {
        val ENDPOINT_BASE_PATH = "/endpoints"
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getEndpoint(@PathParam("id") id: String): Response {
        logger.info("request GET ${Constants.ENDPOINT_BASE_PATH}/${id}")
        val getEndpointResult = getEndpointUseCase.execute(id)
        return if (getEndpointResult.isSuccess) {
            handleSuccessGetEndpoint(id, getEndpointResult)
        } else {
            handleFailureGetOrDeleteEndpoint(id, getEndpointResult.exceptionOrNull() as EndpointException)
        }
    }

    private fun handleSuccessGetEndpoint(id: String, getEndpointResult: Result<Endpoint>): Response {
        val endpoint = getEndpointResult.getOrNull()!!
        logger.info("response GET ${Constants.ENDPOINT_BASE_PATH}/${id} - 200 OK")
        return Response.ok(EndpointMapper.businessToDto(endpoint)).build()
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createEndpoint(endpointInput: EndpointCreateInput): Response {
        logger.info("request POST ${Constants.ENDPOINT_BASE_PATH}")
        logger.debug(LoggerUtils.displayAsJson(endpointInput))

        val validationErrorMessages = EndpointsValidator.validate(endpointInput)
        val createEndpointResult = if (EndpointsValidator.doesNotContainErrors(validationErrorMessages)) {
            createEndpointUseCase.execute(EndpointMapper.dtoToBusiness(endpointInput))
        } else {
            Result.failure(EndpointException(ErrorType.INVALID_INPUT, validationErrorMessages))
        }

        return if (createEndpointResult.isSuccess) {
            handleSuccessCreateEndpoint(createEndpointResult)
        } else {
            handleFailureCreateEndpoint(createEndpointResult.exceptionOrNull() as EndpointException)
        }
    }

    private fun handleSuccessCreateEndpoint(createEndpointResult: Result<Endpoint>): Response {
        val createdEndpoint = createEndpointResult.getOrNull()!!
        logger.info("response POST ${Constants.ENDPOINT_BASE_PATH} - 201 created")
        logger.debug(LoggerUtils.displayAsJson(createdEndpoint))
        val locationUri = URI.create("${Constants.ENDPOINT_BASE_PATH}/${createdEndpoint.id}")
        return Response.created(locationUri).entity(EndpointMapper.businessToDto(createdEndpoint)).build()
    }

    private fun handleFailureCreateEndpoint(e: EndpointException): Response =
        when(e.type) {
            ErrorType.INVALID_INPUT -> {
                logger.info("response POST ${Constants.ENDPOINT_BASE_PATH} - 400 bad request")
                Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
            }
            ErrorType.TECHNICAL_ERROR -> {
                logger.warn("response POST ${Constants.ENDPOINT_BASE_PATH} - 500 server error")
                Response.serverError().entity(e.message).build()
            }
            else -> {
                logger.warn("response POST ${Constants.ENDPOINT_BASE_PATH} - 500 server error")
                logger.error("should not happen")
                Response.serverError().entity(e.message).build()
            }
        }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun updateEndpoint(@PathParam("id") id: String, endpointInput: EndpointUpdateInput): Response {
        logger.info("request PUT ${Constants.ENDPOINT_BASE_PATH} - $endpointInput")
        logger.debug(LoggerUtils.displayAsJson(endpointInput))

        val validationErrorMessages = EndpointsValidator.validate(id, endpointInput)
        val updateEndpointResult = if (EndpointsValidator.doesNotContainErrors(validationErrorMessages)) {
            updateEndpointUseCase.execute(EndpointMapper.dtoToBusiness(endpointInput))
        } else {
            Result.failure(EndpointException(ErrorType.INVALID_INPUT, validationErrorMessages))
        }

        return if (updateEndpointResult.isSuccess) {
            handleSuccessUpdateEndpoint(id, updateEndpointResult)
        } else {
            handleFailureUpdateEndpoint(endpointInput.id, updateEndpointResult.exceptionOrNull() as EndpointException)
        }
    }

    private fun handleSuccessUpdateEndpoint(id: String, updateEndpointResult: Result<Endpoint>): Response {
        val updatedEndpoint = updateEndpointResult.getOrNull()!!
        logger.info("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 200 ok")
        logger.debug(LoggerUtils.displayAsJson(updatedEndpoint))
        return Response.ok().entity(EndpointMapper.businessToDto(updatedEndpoint)).build()
    }

    private fun handleFailureUpdateEndpoint(id: String, e: EndpointException): Response =
        when(e.type) {
            ErrorType.ENTITY_NOT_FOUND -> {
                logger.info("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 404 not found")
                Response.status(404).build()
            }
            ErrorType.INVALID_INPUT -> {
                logger.info("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 400 bad request")
                Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
            }
            ErrorType.TECHNICAL_ERROR -> {
                logger.warn("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 500 server error")
                Response.serverError().entity(e.message).build()
            }
        }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteEndpoint(@PathParam("id") id: String): Response {
        logger.info("request DELETE ${Constants.ENDPOINT_BASE_PATH}/${id}")
        val deleteEndpointResult = deleteEndpointUseCase.execute(id)
        return if (deleteEndpointResult.isSuccess) {
            handleSuccessDeleteEndpoint(id)
        } else {
            handleFailureGetOrDeleteEndpoint(id, deleteEndpointResult.exceptionOrNull() as EndpointException)
        }
    }

    private fun handleSuccessDeleteEndpoint(id: String): Response {
        logger.info("response DELETE ${Constants.ENDPOINT_BASE_PATH}/${id} - 200 ok")
        return Response.ok().build()
    }

    private fun handleFailureGetOrDeleteEndpoint(id: String, e: EndpointException) =
        when(e.type) {
            ErrorType.ENTITY_NOT_FOUND -> {
                logger.info("response GET ${Constants.ENDPOINT_BASE_PATH}/${id} - 404 not found")
                Response.status(404).build()
            }
            ErrorType.TECHNICAL_ERROR -> {
                logger.warn("response GET ${Constants.ENDPOINT_BASE_PATH}/${id} - 500 server error")
                Response.serverError().entity(e.message).build()
            }
            else -> {
                logger.warn("response GET ${Constants.ENDPOINT_BASE_PATH}/${id} - 500 server error")
                logger.error("should not happen")
                Response.serverError().build()
            }
        }
}
