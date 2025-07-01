package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.repository.EndpointRepository

class EndpointUseCases(
    private val repository: EndpointRepository,
) {
    fun create(
        endpoint: Endpoint,
        parentId: Id,
    ): Result<Endpoint> = repository.create(endpoint, parentId)
}
