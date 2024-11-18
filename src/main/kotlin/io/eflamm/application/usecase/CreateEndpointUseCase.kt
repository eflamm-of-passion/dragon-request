package io.eflamm.application.usecase

import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.repository.EndpointRepository


class CreateEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(endpoint: Endpoint): Result<Endpoint> {
        return endpointRepository.createEndpoint(endpoint)
    }
}