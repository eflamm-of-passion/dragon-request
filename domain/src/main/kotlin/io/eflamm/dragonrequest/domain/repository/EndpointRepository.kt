package io.eflamm.dragonrequest.domain.repository

import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.common.Id

interface EndpointRepository {
    fun create(
        endpoint: Endpoint,
        parentId: Id,
    ): Result<Endpoint>
}
