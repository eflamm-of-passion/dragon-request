package io.eflamm.application.usecase

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.Id
import io.eflamm.domain.repository.EndpointRepository

class GetEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(idAsString: String): Result<Endpoint> {
        val id = Id.fromString(idAsString)
        return endpointRepository.getEndpoint(id)
    }
}