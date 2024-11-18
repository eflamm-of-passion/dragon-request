package io.eflamm.dragonrequest.domain.model

import io.eflamm.dragonrequest.domain.model.endpoint.*

// FIXME do no use null, but empty Id instead
class Endpoint(
    val id: Id,
    val protocol: Protocol,
    val domain: DomainName,
    val port: Port,
    val path: Path,
    val queryParameters: QueryParameters
)
