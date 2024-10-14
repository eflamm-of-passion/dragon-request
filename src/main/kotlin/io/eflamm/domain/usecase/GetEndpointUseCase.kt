package io.eflamm.domain.usecase

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.Id
import io.eflamm.domain.repository.EndpointRepository

class GetEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(idAsString: String): Endpoint {
        val id = Id.createFromString(idAsString)
        return endpointRepository.getEndpoint(id)
    }
}