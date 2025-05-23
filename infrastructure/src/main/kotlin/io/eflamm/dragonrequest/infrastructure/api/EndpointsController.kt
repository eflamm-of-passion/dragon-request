package io.eflamm.dragonrequest.infrastructure.api

import io.eflamm.dragonrequest.application.usecase.*
import io.eflamm.dragonrequest.domain.exception.EndpointException
import io.eflamm.dragonrequest.domain.exception.ErrorType
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.infrastructure.api.mapper.EndpointMapper
import io.eflamm.dragonrequest.infrastructure.api.mapper.LoggerUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import java.net.URI

// TODO unit test
// TODO configure the server

class EndpointsController(
    private val getEndpointsUseCase: GetEndpointsUseCase,
    private val getSingleEndpointUseCase: GetSingleEndpointUseCase,
    private val createEndpointUseCase: CreateEndpointUseCase,
    private val updateEndpointUseCase: UpdateEndpointUseCase,
    private val deleteEndpointUseCase: DeleteEndpointUseCase,
    private val logger: Logger
) {
    object Constants {
        const val ENDPOINT_BASE_PATH = "/endpoints"
        const val DEFAULT_FAILURE_MESSAGE = "Should not happen."
    }

    fun startHttpServerOnPort(port: Int) {
        val vertx = Vertx.vertx()
        val router = Router.router(vertx)

        setupRoutes(router)
        createServer(vertx, router, port)
        whenShutdown(vertx)
    }

    private fun setupRoutes(router: Router) {
        addCorsHeaders(router)

        router.route().method(HttpMethod.OPTIONS).handler(this::preflightRequest)

        router.get(Constants.ENDPOINT_BASE_PATH).handler(this::getEndpoints)
        router.get("${Constants.ENDPOINT_BASE_PATH}/:id").handler(this::getEndpoint)
        router.post(Constants.ENDPOINT_BASE_PATH).consumes("*/json").handler(BodyHandler.create())
            .handler(this::createEndpoint)
        router.put("${Constants.ENDPOINT_BASE_PATH}/:id").consumes("*/json").handler(BodyHandler.create())
            .handler(this::updateEndpoint)
        router.delete("${Constants.ENDPOINT_BASE_PATH}/:id").handler(this::deleteEndpoint)
    }

    private fun addCorsHeaders(router: Router) {
        router.route().handler { context ->
            context.response()
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            context.next()
        }
    }

    private fun createServer(vertx: Vertx, router: Router?, port: Int) {
        vertx.createHttpServer()
            .requestHandler(router)
            .listen(port) { result ->
                if (result.succeeded()) {
                    logger.info("Server started on port :$port")
                } else {
                    logger.error("Failed to start the server: ${result.cause()}")
                }
            }
    }

    private fun whenShutdown(vertx: Vertx) {
        Runtime.getRuntime().addShutdownHook(Thread {
            logger.info("Shutting down...")
            vertx.close()
        })
    }

    private fun preflightRequest(context: RoutingContext) {
        logger.info("request OPTIONS ${Constants.ENDPOINT_BASE_PATH}")
        // TODO if allow-origin behind a property
        context.response()
            .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000")
            .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
            .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type")
            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
            .end()
        logger.info("response OPTIONS ${Constants.ENDPOINT_BASE_PATH} - 204 No Content")
    }

    private fun getEndpoints(context: RoutingContext) {
        logger.info("request GET ${Constants.ENDPOINT_BASE_PATH}")
        val getEndpointsResult = getEndpointsUseCase.execute()
        if (getEndpointsResult.isSuccess) {
            handleSuccessGetEndpoints(context, getEndpointsResult)
        } else {
            handleFailureGetEndpoints(context, getEndpointsResult.exceptionOrNull() as EndpointException)
        }
    }

    private fun handleSuccessGetEndpoints(context: RoutingContext, getEndpointResult: Result<List<Endpoint>>) {
        val endpoints = getEndpointResult.getOrNull()!!
        logger.info("response GET ${Constants.ENDPOINT_BASE_PATH} - 200 OK - ${endpoints.size} results")
        context.response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .end(Json.encode(EndpointMapper.businessToDto(endpoints)))
    }

    private fun handleFailureGetEndpoints(context: RoutingContext, e: EndpointException) =
        when (e.type) {
            ErrorType.TECHNICAL_ERROR -> {
                logger.warn("response GET ${Constants.ENDPOINT_BASE_PATH} - 500 server error")
                context.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end(Json.encode("error" to e.message))
            }

            else -> {
                logger.warn("response GET ${Constants.ENDPOINT_BASE_PATH} - 500 server error")
                logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
                context.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end()
            }
        }

    private fun getEndpoint(context: RoutingContext) {
        val id = context.pathParam("id")
        logger.info("request GET ${Constants.ENDPOINT_BASE_PATH}/${id}")
        val getEndpointResult = getSingleEndpointUseCase.execute(id)
        if (getEndpointResult.isSuccess) {
            handleSuccessGetEndpoint(context, id, getEndpointResult)
        } else {
            handleFailureGetOrDeleteEndpoint(context, id, getEndpointResult.exceptionOrNull() as EndpointException)
        }
    }

    private fun handleSuccessGetEndpoint(context: RoutingContext, id: String, getEndpointResult: Result<Endpoint>) {
        val endpoint = getEndpointResult.getOrNull()!!
        logger.info("response GET ${Constants.ENDPOINT_BASE_PATH}/${id} - 200 OK")
        context.response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .end(Json.encode(EndpointMapper.businessToDto(endpoint)))

    }

    private fun createEndpoint(context: RoutingContext) {
        logger.info("request POST ${Constants.ENDPOINT_BASE_PATH}")
        val requestBody: JsonObject = context.body().asJsonObject()
        logger.debug(LoggerUtils.displayAsJson(requestBody.toString()))

        // TODO handle the IllegalArgumentException if body input is not valid
        val endpointInput: EndpointCreateInput = requestBody.mapTo(EndpointCreateInput::class.java)

        val validationErrorMessages = EndpointsValidator.validate(endpointInput)
        val createEndpointResult = if (EndpointsValidator.doesNotContainErrors(validationErrorMessages)) {
            createEndpointUseCase.execute(EndpointMapper.dtoToBusiness(endpointInput))
        } else {
            Result.failure(EndpointException(ErrorType.INVALID_INPUT, validationErrorMessages))
        }

        if (createEndpointResult.isSuccess) {
            handleSuccessCreateEndpoint(context, createEndpointResult)
        } else {
            handleFailureCreateEndpoint(context, createEndpointResult.exceptionOrNull() as EndpointException)
        }
    }

    private fun handleSuccessCreateEndpoint(context: RoutingContext, createEndpointResult: Result<Endpoint>) {
        val createdEndpoint = createEndpointResult.getOrNull()!!
        logger.info("response POST ${Constants.ENDPOINT_BASE_PATH} - 201 created")
        logger.debug(LoggerUtils.displayAsJson(createdEndpoint))
        val locationUri = URI.create("${Constants.ENDPOINT_BASE_PATH}/${createdEndpoint.id}").toString()
        context.response()
            .setStatusCode(HttpResponseStatus.CREATED.code())
            .putHeader("Location", locationUri)
            .end(Json.encode(EndpointMapper.businessToDto(createdEndpoint)))
    }

    private fun handleFailureCreateEndpoint(context: RoutingContext, e: EndpointException) =
        when (e.type) {
            ErrorType.INVALID_INPUT -> {
                logger.info("response POST ${Constants.ENDPOINT_BASE_PATH} - 400 bad request")
                context.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end(Json.encode("error" to e.message))
            }

            ErrorType.TECHNICAL_ERROR -> {
                logger.warn("response POST ${Constants.ENDPOINT_BASE_PATH} - 500 server error")
                context.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end(Json.encode("error" to e.message))
            }

            else -> {
                logger.warn("response POST ${Constants.ENDPOINT_BASE_PATH} - 500 server error")
                logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
                context.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end()
            }
        }

    private fun updateEndpoint(context: RoutingContext) {
        val endpointId = context.pathParam("id")
        logger.info("request PUT ${Constants.ENDPOINT_BASE_PATH}/${endpointId}")
        val requestBody: JsonObject = context.body().asJsonObject()
        logger.debug(LoggerUtils.displayAsJson(requestBody.toString()))

        // TODO handle the IllegalArgumentException if body input is not valid
        val endpointInput: EndpointUpdateInput = requestBody.mapTo(EndpointUpdateInput::class.java)

        val validationErrorMessages = EndpointsValidator.validate(endpointId, endpointInput)
        val updateEndpointResult = if (EndpointsValidator.doesNotContainErrors(validationErrorMessages)) {
            updateEndpointUseCase.execute(EndpointMapper.dtoToBusiness(endpointInput))
        } else {
            Result.failure(EndpointException(ErrorType.INVALID_INPUT, validationErrorMessages))
        }

        if (updateEndpointResult.isSuccess) {
            handleSuccessUpdateEndpoint(context, endpointId, updateEndpointResult)
        } else {
            handleFailureUpdateEndpoint(
                context,
                endpointInput.id,
                updateEndpointResult.exceptionOrNull() as EndpointException
            )
        }
    }

    private fun handleSuccessUpdateEndpoint(
        context: RoutingContext,
        id: String,
        updateEndpointResult: Result<Endpoint>
    ) {
        val updatedEndpoint = updateEndpointResult.getOrNull()!!
        logger.info("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 200 ok")
        logger.debug(LoggerUtils.displayAsJson(updatedEndpoint))
        context.response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .end(Json.encode(EndpointMapper.businessToDto(updatedEndpoint)))
    }

    private fun handleFailureUpdateEndpoint(context: RoutingContext, id: String, e: EndpointException) =
        when (e.type) {
            ErrorType.ENTITY_NOT_FOUND -> {
                logger.info("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 404 not found")
                context.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
            }

            ErrorType.INVALID_INPUT -> {
                logger.info("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 400 bad request")
                context.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end(Json.encode("error" to e.message))
            }

            ErrorType.TECHNICAL_ERROR -> {
                logger.warn("response PUT ${Constants.ENDPOINT_BASE_PATH}/${id} - 500 server error")
                context.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end(Json.encode("error" to e.message))
            }
        }

    private fun deleteEndpoint(context: RoutingContext) {
        val endpointId = context.pathParam("id")
        logger.info("request DELETE ${Constants.ENDPOINT_BASE_PATH}/${endpointId}")
        val deleteEndpointResult = deleteEndpointUseCase.execute(endpointId)
        if (deleteEndpointResult.isSuccess) {
            handleSuccessDeleteEndpoint(context, endpointId)
        } else {
            handleFailureGetOrDeleteEndpoint(
                context,
                endpointId,
                deleteEndpointResult.exceptionOrNull() as EndpointException
            )
        }
    }

    private fun handleSuccessDeleteEndpoint(context: RoutingContext, id: String) {
        logger.info("response DELETE ${Constants.ENDPOINT_BASE_PATH}/${id} - 200 ok")
        context.response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .end()
    }

    private fun handleFailureGetOrDeleteEndpoint(context: RoutingContext, id: String, e: EndpointException) =
        when (e.type) {
            ErrorType.ENTITY_NOT_FOUND -> {
                logger.info("response DELETE ${Constants.ENDPOINT_BASE_PATH}/${id} - 404 not found")
                context.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
            }

            ErrorType.TECHNICAL_ERROR -> {
                logger.warn("response DELETE ${Constants.ENDPOINT_BASE_PATH}/${id} - 500 server error")
                context.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end(Json.encode("error" to e.message))
            }

            else -> {
                logger.warn("response DELETE ${Constants.ENDPOINT_BASE_PATH}/${id} - 500 server error")
                logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end()
            }
        }
}