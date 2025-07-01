package io.eflamm.dragonrequest.infrastructure.api

import io.eflamm.dragonrequest.application.usecase.ApiFileUseCases
import io.eflamm.dragonrequest.application.usecase.CollectionUseCases
import io.eflamm.dragonrequest.application.usecase.EndpointUseCases
import io.eflamm.dragonrequest.application.usecase.WorkspaceUseCases
import io.eflamm.dragonrequest.domain.exception.DragonRequestAppException
import io.eflamm.dragonrequest.domain.exception.ErrorType
import io.eflamm.dragonrequest.domain.model.ApiFile
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.infrastructure.api.mapper.LoggerUtils
import io.eflamm.dragonrequest.infrastructure.api.mapper.toCollection
import io.eflamm.dragonrequest.infrastructure.api.mapper.toDto
import io.eflamm.dragonrequest.infrastructure.api.mapper.toEndpoint
import io.eflamm.dragonrequest.infrastructure.api.mapper.toEndpointOutput
import io.eflamm.dragonrequest.infrastructure.api.mapper.toWorkspace
import io.eflamm.dragonrequest.infrastructure.api.mapper.toWorkspaceOutput
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
    private val apiFileUseCases: ApiFileUseCases,
    private val workspaceUseCases: WorkspaceUseCases,
    private val collectionUseCases: CollectionUseCases,
    private val endpointUseCases: EndpointUseCases,
    private val logger: Logger,
) {
    object Constants {
        const val API_FILES_BASE_PATH = "/api-files"
        const val ENDPOINTS_BASE_PATH = "$API_FILES_BASE_PATH/endpoints"
        const val COLLECTIONS_BASE_PATH = "$API_FILES_BASE_PATH/collection"
        const val WORKSPACES_BASE_PATH = "$API_FILES_BASE_PATH/workspace"
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

        router.get(Constants.API_FILES_BASE_PATH).handler(this::getApiFiles)
//        router.get("${Constants.ENDPOINT_BASE_PATH}/:id").handler(this::getEndpoint)
        router
            .post(Constants.ENDPOINTS_BASE_PATH)
            .consumes("*/json")
            .handler(BodyHandler.create())
            .handler(this::createEndpoint)
//        router
//            .put("${Constants.ENDPOINT_BASE_PATH}/:id")
//            .consumes("*/json")
//            .handler(BodyHandler.create())
//            .handler(this::updateEndpoint)
//        router.delete("${Constants.ENDPOINT_BASE_PATH}/:id").handler(this::deleteEndpoint)
        router
            .post(Constants.COLLECTIONS_BASE_PATH)
            .consumes("*/json")
            .handler(BodyHandler.create())
            .handler(this::createCollection)
        router
            .post(Constants.WORKSPACES_BASE_PATH)
            .consumes("*/json")
            .handler(BodyHandler.create())
            .handler(this::createWorkspace)
    }

    private fun addCorsHeaders(router: Router) {
        router.route().handler { context ->
            context
                .response()
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            context.next()
        }
    }

    private fun createServer(
        vertx: Vertx,
        router: Router?,
        port: Int,
    ) {
        vertx
            .createHttpServer()
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
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("Shutting down...")
                vertx.close()
            },
        )
    }

    private fun preflightRequest(context: RoutingContext) {
        logger.info("request OPTIONS ${Constants.ENDPOINTS_BASE_PATH}")
        // TODO if allow-origin behind a property
        context
            .response()
            .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000")
            .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
            .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type")
            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
            .end()
        logger.info("response OPTIONS ${Constants.ENDPOINTS_BASE_PATH} - 204 No Content")
    }

    private fun getApiFiles(context: RoutingContext) {
        logger.info("request GET ${Constants.API_FILES_BASE_PATH}")
        val getApiFilesResult = apiFileUseCases.getApiFiles()
        if (getApiFilesResult.isSuccess) {
            handleSuccessGetApiFiles(context, getApiFilesResult)
        } else {
            handleFailureGetApiFiles(context, getApiFilesResult.exceptionOrNull() as DragonRequestAppException)
        }
    }

    private fun handleSuccessGetApiFiles(
        context: RoutingContext,
        getApiFilesResult: Result<List<ApiFile>>,
    ) {
        val apiFiles = getApiFilesResult.getOrNull()!!
        logger.info("response GET ${Constants.API_FILES_BASE_PATH} - 200 OK - ${apiFiles.size} results")
        context
            .response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .end(Json.encode(apiFiles.toDto()))
    }

    private fun handleFailureGetApiFiles(
        context: RoutingContext,
        e: DragonRequestAppException,
    ) = when (e.type) {
        ErrorType.TECHNICAL_ERROR -> {
            logger.warn("response GET ${Constants.API_FILES_BASE_PATH} - 500 server error")
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end(Json.encode("error" to e.message))
        }

        else -> {
            logger.warn("response GET ${Constants.API_FILES_BASE_PATH} - 500 server error")
            logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end()
        }
    }

//    private fun getEndpoint(context: RoutingContext) {
//        val id = context.pathParam("id")
//        logger.info("request GET ${Constants.ENDPOINT_BASE_PATH}/$id")
//        val getEndpointResult = getSingleEndpointUseCase.execute(id)
//        if (getEndpointResult.isSuccess) {
//            handleSuccessGetEndpoint(context, id, getEndpointResult)
//        } else {
//            handleFailureGetOrDeleteEndpoint(context, id, getEndpointResult.exceptionOrNull() as DragonRequestAppException)
//        }
//    }
//
//    private fun handleSuccessGetEndpoint(
//        context: RoutingContext,
//        id: String,
//        getEndpointResult: Result<Endpoint>,
//    ) {
//        val endpoint = getEndpointResult.getOrNull()!!
//        logger.info("response GET ${Constants.ENDPOINT_BASE_PATH}/$id - 200 OK")
//        context
//            .response()
//            .setStatusCode(HttpResponseStatus.OK.code())
//            .end(Json.encode(ApiFilesMapper.businessToDto(endpoint)))
//    }

    private fun createEndpoint(context: RoutingContext) {
        logger.info("request POST ${Constants.ENDPOINTS_BASE_PATH}")
        val requestBody: JsonObject = context.body().asJsonObject()
        logger.debug(LoggerUtils.displayAsJson(requestBody.toString()))

        // TODO handle the IllegalArgumentException if body input is not valid
        val endpointInput: EndpointCreateInput = requestBody.mapTo(EndpointCreateInput::class.java)

        val validationErrorMessages = EndpointsValidator.validate(endpointInput)
        val createEndpointResult =
            if (EndpointsValidator.doesNotContainErrors(validationErrorMessages)) {
                endpointUseCases.create(endpointInput.toEndpoint(), Id.fromString(endpointInput.parentId))
            } else {
                Result.failure(DragonRequestAppException(ErrorType.INVALID_INPUT, validationErrorMessages))
            }

        if (createEndpointResult.isSuccess) {
            handleSuccessCreateEndpoint(context, createEndpointResult)
        } else {
            handleFailureCreateEndpoint(context, createEndpointResult.exceptionOrNull() as DragonRequestAppException)
        }
    }

    private fun handleSuccessCreateEndpoint(
        context: RoutingContext,
        createEndpointResult: Result<Endpoint>,
    ) {
        val createdEndpoint = createEndpointResult.getOrNull()!!
        logger.info("response POST ${Constants.ENDPOINTS_BASE_PATH} - 201 created")
        logger.debug(LoggerUtils.displayAsJson(createdEndpoint))
        val locationUri = URI.create("${Constants.ENDPOINTS_BASE_PATH}/${createdEndpoint.id}").toString()
        context
            .response()
            .setStatusCode(HttpResponseStatus.CREATED.code())
            .putHeader("Location", locationUri)
            .end(Json.encode(createdEndpoint.toEndpointOutput()))
    }

    private fun handleFailureCreateEndpoint(
        context: RoutingContext,
        e: DragonRequestAppException,
    ) = when (e.type) {
        ErrorType.INVALID_INPUT -> {
            logger.info("response POST ${Constants.ENDPOINTS_BASE_PATH} - 400 bad request")
            context
                .response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end(Json.encode("error" to e.message))
        }

        ErrorType.TECHNICAL_ERROR -> {
            logger.warn("response POST ${Constants.ENDPOINTS_BASE_PATH} - 500 server error")
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end(Json.encode("error" to e.message))
        }

        else -> {
            logger.warn("response POST ${Constants.ENDPOINTS_BASE_PATH} - 500 server error")
            logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end()
        }
    }
//
//    private fun updateEndpoint(context: RoutingContext) {
//        val endpointId = context.pathParam("id")
//        logger.info("request PUT ${Constants.ENDPOINT_BASE_PATH}/$endpointId")
//        val requestBody: JsonObject = context.body().asJsonObject()
//        logger.debug(LoggerUtils.displayAsJson(requestBody.toString()))
//
//        // TODO handle the IllegalArgumentException if body input is not valid
//        val endpointInput: EndpointUpdateInput = requestBody.mapTo(EndpointUpdateInput::class.java)
//
//        val validationErrorMessages = EndpointsValidator.validate(endpointId, endpointInput)
//        val updateEndpointResult =
//            if (EndpointsValidator.doesNotContainErrors(validationErrorMessages)) {
//                updateEndpointUseCase.execute(ApiFilesMapper.dtoToBusiness(endpointInput))
//            } else {
//                Result.failure(DragonRequestAppException(ErrorType.INVALID_INPUT, validationErrorMessages))
//            }
//
//        if (updateEndpointResult.isSuccess) {
//            handleSuccessUpdateEndpoint(context, endpointId, updateEndpointResult)
//        } else {
//            handleFailureUpdateEndpoint(
//                context,
//                endpointInput.id,
//                updateEndpointResult.exceptionOrNull() as DragonRequestAppException,
//            )
//        }
//    }
//
//    private fun handleSuccessUpdateEndpoint(
//        context: RoutingContext,
//        id: String,
//        updateEndpointResult: Result<Endpoint>,
//    ) {
//        val updatedEndpoint = updateEndpointResult.getOrNull()!!
//        logger.info("response PUT ${Constants.ENDPOINT_BASE_PATH}/$id - 200 ok")
//        logger.debug(LoggerUtils.displayAsJson(updatedEndpoint))
//        context
//            .response()
//            .setStatusCode(HttpResponseStatus.OK.code())
//            .end(Json.encode(ApiFilesMapper.businessToDto(updatedEndpoint)))
//    }

    private fun handleFailureUpdateEndpoint(
        context: RoutingContext,
        id: String,
        e: DragonRequestAppException,
    ) = when (e.type) {
        ErrorType.ENTITY_NOT_FOUND -> {
            logger.info("response PUT ${Constants.ENDPOINTS_BASE_PATH}/$id - 404 not found")
            context.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
        }

        ErrorType.INVALID_INPUT -> {
            logger.info("response PUT ${Constants.ENDPOINTS_BASE_PATH}/$id - 400 bad request")
            context
                .response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end(Json.encode("error" to e.message))
        }

        ErrorType.TECHNICAL_ERROR -> {
            logger.warn("response PUT ${Constants.ENDPOINTS_BASE_PATH}/$id - 500 server error")
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end(Json.encode("error" to e.message))
        }
    }

//    private fun deleteEndpoint(context: RoutingContext) {
//        val endpointId = context.pathParam("id")
//        logger.info("request DELETE ${Constants.ENDPOINT_BASE_PATH}/$endpointId")
//        val deleteEndpointResult = deleteEndpointUseCase.execute(endpointId)
//        if (deleteEndpointResult.isSuccess) {
//            handleSuccessDeleteEndpoint(context, endpointId)
//        } else {
//            handleFailureGetOrDeleteEndpoint(
//                context,
//                endpointId,
//                deleteEndpointResult.exceptionOrNull() as DragonRequestAppException,
//            )
//        }
//    }
//
//    private fun handleSuccessDeleteEndpoint(
//        context: RoutingContext,
//        id: String,
//    ) {
//        logger.info("response DELETE ${Constants.ENDPOINT_BASE_PATH}/$id - 200 ok")
//        context
//            .response()
//            .setStatusCode(HttpResponseStatus.OK.code())
//            .end()
//    }
//
//    private fun handleFailureGetOrDeleteEndpoint(
//        context: RoutingContext,
//        id: String,
//        e: DragonRequestAppException,
//    ) = when (e.type) {
//        ErrorType.ENTITY_NOT_FOUND -> {
//            logger.info("response DELETE ${Constants.ENDPOINT_BASE_PATH}/$id - 404 not found")
//            context.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
//        }
//
//        ErrorType.TECHNICAL_ERROR -> {
//            logger.warn("response DELETE ${Constants.ENDPOINT_BASE_PATH}/$id - 500 server error")
//            context
//                .response()
//                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
//                .end(Json.encode("error" to e.message))
//        }
//
//        else -> {
//            logger.warn("response DELETE ${Constants.ENDPOINT_BASE_PATH}/$id - 500 server error")
//            logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
//            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end()
//        }
//    }

    private fun createCollection(context: RoutingContext) {
        logger.info("request POST ${Constants.COLLECTIONS_BASE_PATH}")
        val requestBody: JsonObject = context.body().asJsonObject()
        logger.debug(LoggerUtils.displayAsJson(requestBody.toString()))

        // TODO handle the IllegalArgumentException if body input is not valid
        val collectionInput: CollectionCreateInput = requestBody.mapTo(CollectionCreateInput::class.java)

        val validationErrorMessages = CollectionsValidator.validate(collectionInput)
        val createCollectionResult =
            if (CollectionsValidator.doesNotContainErrors(validationErrorMessages)) {
                collectionUseCases.create(collectionInput.toCollection(), Id.fromString(collectionInput.parentId))
            } else {
                Result.failure(DragonRequestAppException(ErrorType.INVALID_INPUT, validationErrorMessages))
            }
        if (createCollectionResult.isSuccess) {
            handleSuccessCreateCollection(context, createCollectionResult)
        } else {
            handleFailureCreateCollection(context, createCollectionResult.exceptionOrNull() as DragonRequestAppException)
        }
    }

    private fun handleSuccessCreateCollection(
        context: RoutingContext,
        createResult: Result<Collection>,
    ) {
        val createdCollection = createResult.getOrNull()!!
        logger.info("response POST ${Constants.COLLECTIONS_BASE_PATH} - 201 created")
        logger.debug(LoggerUtils.displayAsJson(createdCollection))
        val locationUri = URI.create("${Constants.COLLECTIONS_BASE_PATH}/${createdCollection.id}").toString()
        context
            .response()
            .setStatusCode(HttpResponseStatus.CREATED.code())
            .putHeader("Location", locationUri)
            .end(Json.encode(createdCollection.toDto()))
    }

    private fun handleFailureCreateCollection(
        context: RoutingContext,
        e: DragonRequestAppException,
    ) = when (e.type) {
        ErrorType.INVALID_INPUT -> {
            logger.info("response POST ${Constants.COLLECTIONS_BASE_PATH} - 400 bad request")
            context
                .response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end(Json.encode(JsonObject().put("error", e.message)))
        }

        ErrorType.TECHNICAL_ERROR -> {
            logger.warn("response POST ${Constants.WORKSPACES_BASE_PATH} - 500 server error")
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end(Json.encode(JsonObject().put("error", e.message)))
        }

        else -> {
            logger.warn("response POST ${Constants.WORKSPACES_BASE_PATH} - 500 server error")
            logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end()
        }
    }

    private fun createWorkspace(context: RoutingContext) {
        logger.info("request POST ${Constants.WORKSPACES_BASE_PATH}")
        val requestBody: JsonObject = context.body().asJsonObject()
        logger.debug(LoggerUtils.displayAsJson(requestBody.toString()))

        // TODO handle the IllegalArgumentException if body input is not valid
        val workspaceInput: WorkspaceCreateInput = requestBody.mapTo(WorkspaceCreateInput::class.java)

        val validationErrorMessages = WorkspacesValidator.validate(workspaceInput)
        val createWorkspaceResult =
            if (WorkspacesValidator.doesNotContainErrors(validationErrorMessages)) {
                workspaceUseCases.create(workspaceInput.toWorkspace())
            } else {
                Result.failure(DragonRequestAppException(ErrorType.INVALID_INPUT, validationErrorMessages))
            }
        if (createWorkspaceResult.isSuccess) {
            handleSuccessCreateWorkspace(context, createWorkspaceResult)
        } else {
            handleFailureCreateWorkspace(context, createWorkspaceResult.exceptionOrNull() as DragonRequestAppException)
        }
    }

    private fun handleSuccessCreateWorkspace(
        context: RoutingContext,
        createResult: Result<Workspace>,
    ) {
        val createdWorkspace = createResult.getOrNull()!!
        logger.info("response POST ${Constants.WORKSPACES_BASE_PATH} - 201 created")
        logger.debug(LoggerUtils.displayAsJson(createdWorkspace))
        val locationUri = URI.create("${Constants.WORKSPACES_BASE_PATH}/${createdWorkspace.id}").toString()
        context
            .response()
            .setStatusCode(HttpResponseStatus.CREATED.code())
            .putHeader("Location", locationUri)
            .end(Json.encode(createdWorkspace.toWorkspaceOutput()))
    }

    private fun handleFailureCreateWorkspace(
        context: RoutingContext,
        e: DragonRequestAppException,
    ) = when (e.type) {
        ErrorType.INVALID_INPUT -> {
            logger.info("response POST ${Constants.WORKSPACES_BASE_PATH} - 400 bad request")
            context
                .response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end(Json.encode(JsonObject().put("error", e.message)))
        }

        ErrorType.TECHNICAL_ERROR -> {
            logger.warn("response POST ${Constants.WORKSPACES_BASE_PATH} - 500 server error")
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end(Json.encode(JsonObject().put("error", e.message)))
        }

        else -> {
            logger.warn("response POST ${Constants.WORKSPACES_BASE_PATH} - 500 server error")
            logger.error(Constants.DEFAULT_FAILURE_MESSAGE)
            context
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end()
        }
    }
}
