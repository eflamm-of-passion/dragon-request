package io.eflamm.dragonrequest.infrastructure.api

import io.eflamm.dragonrequest.infrastructure.api.dto.EndpointCreateInput
import io.eflamm.dragonrequest.infrastructure.api.dto.EndpointUpdateInput
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.util.stream.Collectors

// TODO should unit test
abstract class EndpointsValidator {
    // TODO make sure the parentId is an Id
    companion object Methods {
        fun validate(input: EndpointCreateInput): String = validateUrlFormat(input.url)

        fun validate(
            id: String,
            input: EndpointUpdateInput,
        ): String =
            listOf(validateUrlFormat(input.url), areIdsSame(id, input.id))
                .stream()
                .collect(Collectors.joining())

        fun doesNotContainErrors(errorMessage: String): Boolean = errorMessage.isEmpty()

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
    }
}
