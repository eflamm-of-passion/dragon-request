package io.eflamm.application.mapper

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import io.eflamm.infrastructure.api.EndpointCreateInput
import io.eflamm.infrastructure.api.EndpointOutput
import io.eflamm.infrastructure.api.EndpointUpdateInput
import java.net.URI

class EndpointMapper {
    companion object {
        private const val OMITTED_DEFAULT_PORT = ""
        private const val PROTOCOL_TO_DOMAIN_SEPARATOR = "://"
        private const val DOMAIN_TO_PORT_SEPARATOR = ":"

        fun dtoToBusiness(endpointCreateInput: EndpointCreateInput): Endpoint {
            val uri = URI(endpointCreateInput.url)
            return Endpoint(
                Id.create(),
                Protocol.fromString(uri.scheme),
                DomainName(uri.host),
                parsePortFromUri(uri),
                parsePathFromUri(uri),
                parseQueryParametersFromUri(uri)
            )
        }

        fun dtoToBusiness(endpointUpdateInput: EndpointUpdateInput): Endpoint {
            val uri = URI(endpointUpdateInput.url)
            return Endpoint(
                Id.fromString(endpointUpdateInput.id),
                Protocol.fromString(uri.scheme),
                DomainName(uri.host),
                parsePortFromUri(uri),
                parsePathFromUri(uri),
                parseQueryParametersFromUri(uri)
            )
        }

        fun businessToDto(endpoint: Endpoint): EndpointOutput {
            var aggregatedUrl = endpoint.protocol.value
            aggregatedUrl += PROTOCOL_TO_DOMAIN_SEPARATOR
            aggregatedUrl += endpoint.domain.get()
            if(endpoint.port.isDefaultPort()) {
                aggregatedUrl += OMITTED_DEFAULT_PORT
            } else {
                aggregatedUrl += DOMAIN_TO_PORT_SEPARATOR
                aggregatedUrl += endpoint.port.get()
            }
            aggregatedUrl += endpoint.path.aggregate()
            aggregatedUrl += endpoint.queryParameters.aggregate()
            return EndpointOutput(endpoint.id.get(), aggregatedUrl)
        }

        private fun parsePortFromUri(uri: URI): Port {
            var port = 80
            if (uri.port == -1) {
                when(uri.scheme) {
                    "http" -> port = 80
                    "https" -> port = 443
                }
            } else {
                port = uri.port
            }
            return Port(port)
        }

        private fun parsePathFromUri(uri: URI): Path {
            return if(uri.path == null || uri.path.isEmpty()) {
                Path.create()
            } else {
                Path.fromString(uri.path)
            }
        }

        private fun parseQueryParametersFromUri(uri: URI): QueryParameters {
            return if(uri.query == null || uri.query.isEmpty()) {
                QueryParameters.create()
            } else {
                QueryParameters.fromString(uri.query)
            }
        }
    }
}