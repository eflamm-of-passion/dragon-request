package io.eflamm.infrastructure

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*

abstract class EndpointUtils {
    companion object {
        fun createEndpointWitRandomId(): Endpoint {
            return Endpoint(
                Id.create(),
                Protocol.HTTP,
                DomainName("acme.org"),
                Port(80),
                Path(listOf("path", "more")),
                QueryParameters(mapOf("one" to "foo", "two" to "bar"))
            )
        }
    }

}