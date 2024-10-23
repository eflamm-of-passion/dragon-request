package io.eflamm.infrastructure.cdi

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.infrastructure.api.EndpointsController
import io.eflamm.infrastructure.persistence.MockEndpointRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class TestApplicationDepencyInjector {

    @Produces
    @TestQualifier
    fun instantiateController(): EndpointsController {
        return EndpointsController(instantiateGetEndpointUseCase(), instantiateCreateEndpointUseCase())
    }

    @Produces
    @TestQualifier
    fun instantiateGetEndpointUseCase(): GetEndpointUseCase {
        val endpointRepository = MockEndpointRepository()
        return GetEndpointUseCase(endpointRepository)
    }

    @Produces
    @TestQualifier
    fun instantiateCreateEndpointUseCase(): CreateEndpointUseCase {
        val endpointRepository = MockEndpointRepository()
        return CreateEndpointUseCase(endpointRepository)
    }
}