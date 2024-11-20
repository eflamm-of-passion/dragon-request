package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.repository.EndpointRepository

class GetEndpointsUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(): Result<List<Endpoint>> {
        return endpointRepository.getEndpoints()
    }
}
