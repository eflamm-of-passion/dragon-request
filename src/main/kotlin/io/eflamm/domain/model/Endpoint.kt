package io.eflamm.domain.model

import io.eflamm.domain.model.endpoint.DomainName
import io.eflamm.domain.model.endpoint.Id
import io.eflamm.domain.model.endpoint.Path
import io.eflamm.domain.model.endpoint.Port
import io.eflamm.domain.model.endpoint.Protocol
import io.eflamm.domain.model.endpoint.QueryParameters

// FIXME do no use null, but empty Id instead
class Endpoint(
    val id: Id,
    val protocol: Protocol,
    val domain: DomainName,
    val port: Port,
    val path: Path,
    val queryParameters: QueryParameters
)
