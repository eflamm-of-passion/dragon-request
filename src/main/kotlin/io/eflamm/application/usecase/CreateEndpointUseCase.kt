package io.eflamm.application.usecase

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.repository.EndpointRepository

class CreateEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(endpoint: Endpoint): Result<Endpoint> {
        return endpointRepository.createEndpoint(endpoint)
    }
}