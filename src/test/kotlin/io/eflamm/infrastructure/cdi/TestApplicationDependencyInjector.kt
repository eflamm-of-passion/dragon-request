package io.eflamm.infrastructure.cdi

import io.eflamm.dragonrequest.application.usecase.*
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.domain.repository.EndpointRepository
import io.eflamm.dragonrequest.logger.slf4j.SLF4JLogger
import io.eflamm.dragonrequest.repository.sqlite.SqliteRepository
import io.eflamm.infrastructure.api.EndpointsController
import io.eflamm.infrastructure.cdi.properties.ApplicationPropertyProvider
import io.eflamm.infrastructure.cdi.properties.PropertyProvider
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
        return EndpointsController(
            instantiateGetEndpointsUseCase(),
            instantiateSingleGetEndpointUseCase(),
            instantiateCreateEndpointUseCase(),
            instantiateUpdateEndpointUseCase(),
            instantiateDeleteEndpointUseCase(),
            instantiateLogger()
        )
    }

    @Produces
    fun instantiateGetEndpointsUseCase(): GetEndpointsUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return GetEndpointsUseCase(endpointRepository)
    }

    @Produces
    fun instantiateSingleGetEndpointUseCase(): GetSingleEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return GetSingleEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateCreateEndpointUseCase(): CreateEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return CreateEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateUpdateEndpointUseCase(): UpdateEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return UpdateEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateDeleteEndpointUseCase(): DeleteEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return DeleteEndpointUseCase(endpointRepository)
    }

    private fun instantiateRepositoryImpl(propertyProvider: PropertyProvider): EndpointRepository {
        if (!this::repository.isInitialized) {
            repository = SqliteRepository(":memory:")
            (repository as SqliteRepository).connect()
        }
        return repository
    }

    private fun instantiatePropertyProviderImpl(): PropertyProvider {
        return ApplicationPropertyProvider("application-dev.properties")
    }

    @Produces
    fun instantiateLogger(): Logger {
        return SLF4JLogger()
    }

    @PreDestroy
    fun shutDown() {
        if (this::repository.isInitialized){
            (repository as SqliteRepository).disconnect()
        }
    }
}
