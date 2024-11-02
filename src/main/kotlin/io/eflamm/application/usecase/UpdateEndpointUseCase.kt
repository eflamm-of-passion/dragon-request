package io.eflamm.application.usecase

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.repository.EndpointRepository

class UpdateEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(endpoint: Endpoint): Result<Endpoint> {
        return endpointRepository.updateEndpoint(endpoint)
    }
}