package io.eflamm.domain.model

import io.eflamm.domain.model.endpoint.DomainName
import io.eflamm.domain.model.endpoint.Port
import io.eflamm.domain.model.endpoint.Protocol
import io.eflamm.io.eflamm.domain.model.endpoint.Id
import io.eflamm.io.eflamm.domain.model.endpoint.Path
import io.eflamm.io.eflamm.domain.model.endpoint.QueryParameters

class Endpoint(id: Id, protocol: Protocol, domain: DomainName, port: Port, path: Path, queryParameters: QueryParameters) {


}
