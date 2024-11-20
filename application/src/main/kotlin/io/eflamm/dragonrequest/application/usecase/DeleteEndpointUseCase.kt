package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.endpoint.Id
import io.eflamm.dragonrequest.domain.repository.EndpointRepository

class DeleteEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(idAsString: String): Result<Unit> {
        val id = Id.fromString(idAsString)
        return endpointRepository.deleteEndpoint(id)
    }
}