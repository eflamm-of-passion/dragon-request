package io.eflamm.application.usecase

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.Id
import io.eflamm.domain.repository.EndpointRepository

class GetEndpointsUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(): Result<List<Endpoint>> {
        return endpointRepository.getEndpoints()
    }
}
