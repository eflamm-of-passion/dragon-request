package io.eflamm.dragonrequest.infrastructure.api

import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.stream.Collectors

abstract class EndpointsValidator {
    companion object Methods {
        fun validate(input: EndpointCreateInput): String {
            return validateUrlFormat(input.url)
        }

        fun validate(id: String, input: EndpointUpdateInput): String {
            return listOf(validateUrlFormat(input.url), areIdsSame(id, input.id)).stream()
                .collect(Collectors.joining())
        }

        fun doesNotContainErrors(errorMessage: String): Boolean {
            return errorMessage.isEmpty()
        }

        private fun areIdsSame(idFromPath: String, idFromBody: String): String {
            return if(idFromPath == idFromBody) {
                ""
            } else {
                "The ids from path and body do not match: $idFromPath and $idFromBody. "
            }
        }

        private fun validateUrlFormat(urlAsString: String): String {
            var errorMessage = ""
            try {
                URI(urlAsString).toURL()
                return errorMessage
            } catch (e: Exception) {
                errorMessage = when(e) {
                    is IllegalArgumentException, is MalformedURLException, is URISyntaxException ->  {
                        "The URL is malformed:  $urlAsString. "
                    }
                    else -> "Unhandled exception"
                }
                return errorMessage
            }

        }
    }
}
