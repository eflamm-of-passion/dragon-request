package io.eflamm.domain.usecase

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.Id
import io.eflamm.domain.repository.EndpointRepository

class CreateEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(endpoint: Endpoint): Endpoint {
        return endpointRepository.createEndpoint(endpoint)
    }
}