package io.eflamm.dragonrequest.infrastructure.api

import io.eflamm.dragonrequest.infrastructure.api.dto.ApiFileCreateInput
import io.eflamm.dragonrequest.infrastructure.api.dto.EndpointUpdateInput
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.util.stream.Collectors

// TODO should unit test

fun ApiFileCreateInput.Endpoint.validate(): String = validateUrlFormat(url)

fun ApiFileCreateInput.Collection.validate(): String = listOf(isNameBlank(name)).join()

fun ApiFileCreateInput.Workspace.validate(): String = listOf(isNameBlank(name)).join()

fun EndpointUpdateInput.validate(
    id: String,
): String = listOf(validateUrlFormat(url), areIdsSame(id, id)).join()

private fun List<String>.join() = stream().collect(Collectors.joining())

private fun areIdsSame(
    idFromPath: String,
    idFromBody: String,
): String =
    if (idFromPath == idFromBody) {
        ""
    } else {
        "The ids from path and body do not match: $idFromPath and $idFromBody. "
    }

private fun validateUrlFormat(urlAsString: String): String {
    var errorMessage = ""
    try {
        URI(urlAsString).toURL()
        return errorMessage
    } catch (e: Exception) {
        errorMessage =
            when (e) {
                is IllegalArgumentException, is MalformedURLException, is URISyntaxException -> {
                    "The URL is malformed:  $urlAsString. "
                }
                else -> "Unhandled exception"
            }
        return errorMessage
    }
}

private fun isNameBlank(name: String): String =
    if (name.isBlank()) {
        "Name cannot be blank. "
    } else {
        ""
    }

fun doesNotContainErrors(errorMessage: String): Boolean = errorMessage.isEmpty()
