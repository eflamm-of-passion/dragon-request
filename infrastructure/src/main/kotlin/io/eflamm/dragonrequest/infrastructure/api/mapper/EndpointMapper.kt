package io.eflamm.dragonrequest.infrastructure.api.mapper

import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.endpoint.DomainName
import io.eflamm.dragonrequest.domain.model.endpoint.HttpMethod
import io.eflamm.dragonrequest.domain.model.endpoint.Id
import io.eflamm.dragonrequest.domain.model.endpoint.Path
import io.eflamm.dragonrequest.domain.model.endpoint.Port
import io.eflamm.dragonrequest.domain.model.endpoint.Protocol
import io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters
import io.eflamm.dragonrequest.infrastructure.api.EndpointCreateInput
import io.eflamm.dragonrequest.infrastructure.api.EndpointOutput
import io.eflamm.dragonrequest.infrastructure.api.EndpointUpdateInput
import java.net.URI

class EndpointMapper {
    companion object {
        private const val OMITTED_DEFAULT_PORT = ""
        private const val PROTOCOL_TO_DOMAIN_SEPARATOR = "://"
        private const val DOMAIN_TO_PORT_SEPARATOR = ":"
        private const val DEFAULT_HTTP_PORT = 80
        private const val DEFAULT_HTTPS_PORT = 443

        fun dtoToBusiness(endpointCreateInput: EndpointCreateInput): Endpoint {
            val uri = URI(endpointCreateInput.url)
            return Endpoint(
                Id.create(),
                HttpMethod.valueOf(endpointCreateInput.httpMethod),
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
                HttpMethod.valueOf(endpointUpdateInput.httpMethod),
                Protocol.fromString(uri.scheme),
                DomainName(uri.host),
                parsePortFromUri(uri),
                parsePathFromUri(uri),
                parseQueryParametersFromUri(uri)
            )
        }

        fun businessToDto(endpoints: List<Endpoint>): List<EndpointOutput> = endpoints.map { businessToDto(it) }

        fun businessToDto(endpoint: Endpoint): EndpointOutput = EndpointOutput(
            endpoint.id.get(),
            endpoint.httpMethod.toString(),
            aggregateUrl(endpoint)
        )


        private fun aggregateUrl(endpoint: Endpoint): String {
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

        private fun parsePortFromUri(uri: URI): Port {
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

    }
}
