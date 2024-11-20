package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.endpoint.Id
import io.eflamm.dragonrequest.domain.repository.EndpointRepository

class GetSingleEndpointUseCase(private val endpointRepository: EndpointRepository) {
    fun execute(idAsString: String): Result<Endpoint> {
        val id = Id.fromString(idAsString)
        return endpointRepository.getEndpoint(id)
    }
}
