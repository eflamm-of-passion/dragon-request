package io.eflamm.dragonrequest.infrastructure.api.mapper

import io.eflamm.dragonrequest.domain.model.ApiFile
import io.eflamm.dragonrequest.domain.model.ApiFilename
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.model.endpoint.DomainName
import io.eflamm.dragonrequest.domain.model.endpoint.HttpMethod
import io.eflamm.dragonrequest.domain.model.endpoint.Path
import io.eflamm.dragonrequest.domain.model.endpoint.Port
import io.eflamm.dragonrequest.domain.model.endpoint.Protocol
import io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters
import io.eflamm.dragonrequest.infrastructure.api.dto.ApiFileCreateInput
import io.eflamm.dragonrequest.infrastructure.api.dto.ApiFileOutput
import io.eflamm.dragonrequest.infrastructure.api.dto.CollectionOutput
import io.eflamm.dragonrequest.infrastructure.api.dto.EndpointOutput
import io.eflamm.dragonrequest.infrastructure.api.dto.WorkspaceOutput
import java.net.URI

fun ApiFile.toDto(): ApiFileOutput =
    when (this) {
        is Workspace -> toWorkspaceOutput()
        is Collection -> toCollectionOutput()
        is Endpoint -> toEndpointOutput()
        else -> throw IllegalStateException("Unknown ApiFile type")
    } as ApiFileOutput

fun List<ApiFile>.toDto(): List<ApiFileOutput> = map { it.toDto() }

fun ApiFileCreateInput.Workspace.toWorkspace(): Workspace = Workspace(Id.create(), ApiFilename(name))

fun Workspace.toWorkspaceOutput(): WorkspaceOutput =
    WorkspaceOutput(
        id.toString(),
        name.toString(),
        files = getFiles().map { it.toDto() },
    )

fun ApiFileCreateInput.Collection.toCollection(): Collection = Collection(Id.create(), ApiFilename(name))

fun Collection.toCollectionOutput(): CollectionOutput =
    CollectionOutput(
        id.toString(),
        name.toString(),
        files = getFiles().map { it.toDto() },
    )

fun ApiFileCreateInput.Endpoint.toEndpoint(): Endpoint {
    val uri = URI(url)
    return Endpoint(
        Id.create(),
        ApiFilename(name),
        HttpMethod.valueOf(httpMethod),
        Protocol.fromString(uri.scheme),
        DomainName(uri.host),
        parsePortFromUri(uri),
        parsePathFromUri(uri),
        parseQueryParametersFromUri(uri),
    )
}

fun Endpoint.toEndpointOutput(): EndpointOutput =
    EndpointOutput(
        id.toString(),
        name.value,
        httpMethod.toString(),
        aggregateUrl(this),
    )

private fun parsePortFromUri(uri: URI): Port {
    val DEFAULT_HTTP_PORT = 80
    val DEFAULT_HTTPS_PORT = 443

    var port = DEFAULT_HTTP_PORT
    if (uri.port == -1) {
        when (uri.scheme) {
            "http" -> port = DEFAULT_HTTP_PORT
            "https" -> port = DEFAULT_HTTPS_PORT
        }
    } else {
        port = uri.port
    }
    return Port(port)
}

private fun parsePathFromUri(uri: URI): Path =
    if (uri.path == null || uri.path.isEmpty()) {
        Path.create()
    } else {
        Path.fromString(uri.path)
    }

private fun parseQueryParametersFromUri(uri: URI): QueryParameters =
    if (uri.query == null || uri.query.isEmpty()) {
        QueryParameters.create()
    } else {
        QueryParameters.fromString(uri.query)
    }

private fun aggregateUrl(endpoint: Endpoint): String {
    val OMITTED_DEFAULT_PORT = ""
    val PROTOCOL_TO_DOMAIN_SEPARATOR = "://"
    val DOMAIN_TO_PORT_SEPARATOR = ":"

    var aggregatedUrl: String = endpoint.protocol.value
    aggregatedUrl += PROTOCOL_TO_DOMAIN_SEPARATOR
    aggregatedUrl += endpoint.domain.get()
    if (endpoint.port.isDefaultPort()) {
        aggregatedUrl += OMITTED_DEFAULT_PORT
    } else {
        aggregatedUrl += DOMAIN_TO_PORT_SEPARATOR
        aggregatedUrl += endpoint.port.get()
    }
    aggregatedUrl += endpoint.path.aggregate()
    aggregatedUrl += endpoint.queryParameters.aggregate()
    return aggregatedUrl
}
