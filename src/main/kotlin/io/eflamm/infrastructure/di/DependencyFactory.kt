package io.eflamm.infrastructure.di

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.infrastructure.api.EndpointsController
import io.eflamm.infrastructure.persistence.MockEndpointRepository
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.enterprise.inject.Produces


//@ApplicationScoped
class DependencyFactory {

    private lateinit var endpointsController: EndpointsController

//    @Produces
    fun createEndpointsController(@Observes ev: StartupEvent) {
        val endpointRepository = MockEndpointRepository()
        val getEndpointUseCase = GetEndpointUseCase(endpointRepository)
        val createEndpointUseCase = CreateEndpointUseCase(endpointRepository)
//        endpointsController = EndpointsController(getEndpointUseCase, createEndpointUseCase)
    }

}