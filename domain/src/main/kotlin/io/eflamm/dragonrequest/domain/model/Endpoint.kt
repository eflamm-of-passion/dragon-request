package io.eflamm.dragonrequest.domain.model

import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.model.endpoint.DomainName
import io.eflamm.dragonrequest.domain.model.endpoint.HttpMethod
import io.eflamm.dragonrequest.domain.model.endpoint.Path
import io.eflamm.dragonrequest.domain.model.endpoint.Port
import io.eflamm.dragonrequest.domain.model.endpoint.Protocol
import io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters

// FIXME do no use null, but empty Id instead
class Endpoint(
    id: Id,
    name: ApiFilename,
    val httpMethod: HttpMethod,
    val protocol: Protocol,
    val domain: DomainName,
    val port: Port,
    val path: Path,
    val queryParameters: QueryParameters,
) : InternalApiFile(id, name)
