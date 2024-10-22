package io.eflamm.infrastructure.di

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.infrastructure.api.EndpointsController
import io.eflamm.infrastructure.persistence.MockEndpointRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces


@ApplicationScoped
class ApplicationDepencyInjector {

    @Produces
    fun instantiateController(): EndpointsController {
        return EndpointsController(instantiateGetEndpointUseCase(), instantiateCreateEndpointUseCase())
    }

    @Produces
    fun instantiateGetEndpointUseCase(): GetEndpointUseCase {
        val endpointRepository = MockEndpointRepository()
        return GetEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateCreateEndpointUseCase(): CreateEndpointUseCase {
        val endpointRepository = MockEndpointRepository()
        return CreateEndpointUseCase(endpointRepository)
    }

}