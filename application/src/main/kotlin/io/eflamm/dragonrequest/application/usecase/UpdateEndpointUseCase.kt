package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.repository.EndpointRepository

class UpdateEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(endpoint: Endpoint): Result<Endpoint> {
        return endpointRepository.updateEndpoint(endpoint)
    }
}