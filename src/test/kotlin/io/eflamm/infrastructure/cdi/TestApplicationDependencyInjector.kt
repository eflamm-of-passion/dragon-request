package io.eflamm.infrastructure.cdi

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.domain.repository.EndpointRepository
import io.eflamm.infrastructure.api.EndpointsController
import io.eflamm.infrastructure.persistence.SqliteRepository
import jakarta.annotation.PreDestroy
import jakarta.annotation.Priority
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Alternative
import jakarta.enterprise.inject.Produces

@Alternative
@Priority(1)
@ApplicationScoped
class TestApplicationDependencyInjector {

    private lateinit var repository: EndpointRepository

    @Produces
    fun instantiateController(): EndpointsController {
        return EndpointsController(instantiateGetEndpointUseCase(), instantiateCreateEndpointUseCase())
    }

    @Produces
    fun instantiateGetEndpointUseCase(): GetEndpointUseCase {
        val endpointRepository = getRepositoryImpl()
        return GetEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateCreateEndpointUseCase(): CreateEndpointUseCase {
        val endpointRepository = getRepositoryImpl()
        return CreateEndpointUseCase(endpointRepository)
    }

    private fun getRepositoryImpl(): EndpointRepository {
        if (!this::repository.isInitialized) {
            repository = SqliteRepository()
            (repository as SqliteRepository).connect()
        }
        return repository
    }

    @PreDestroy
    fun shutDown() {
        if (this::repository.isInitialized){
            (repository as SqliteRepository).disconnect()
        }
    }
}