package io.eflamm.application.usecase

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.Id
import io.eflamm.domain.repository.EndpointRepository

class DeleteEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(idAsString: String): Result<Unit> {
        val id = Id.fromString(idAsString)
        return endpointRepository.deleteEndpoint(id)
    }
}