package io.eflamm.infrastructure

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*

abstract class EndpointUtils {
    companion object {
        fun createEndpointWithoutId(): Endpoint {
            return Endpoint(
                null,
                Protocol.HTTP,
                DomainName("acme.org"),
                Port(80),
                Path.create(),
                QueryParameters.create()
            )
        }
    }

}