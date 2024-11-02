package io.eflamm.infrastructure.cdi

import io.eflamm.application.usecase.CreateEndpointUseCase
import io.eflamm.application.usecase.DeleteEndpointUseCase
import io.eflamm.application.usecase.GetEndpointUseCase
import io.eflamm.application.usecase.UpdateEndpointUseCase
import io.eflamm.domain.repository.EndpointRepository
import io.eflamm.infrastructure.api.EndpointsController
import io.eflamm.infrastructure.cdi.properties.ApplicationPropertyProvider
import io.eflamm.infrastructure.cdi.properties.PropertyProvider
import io.eflamm.infrastructure.persistence.SqliteRepository
import jakarta.annotation.PreDestroy
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces


@ApplicationScoped
class ApplicationDepencyInjector {

    private lateinit var repository: EndpointRepository

    @Produces
    fun instantiateController(): EndpointsController {
        return EndpointsController(
            instantiateGetEndpointUseCase(),
            instantiateCreateEndpointUseCase(),
            instantiateUpdateEndpointUseCase(),
            instantiateDeleteEndpointUseCase()
        )
    }

    @Produces
    fun instantiateGetEndpointUseCase(): GetEndpointUseCase {
        val endpointRepository = getRepositoryImpl(getPropertyProviderImpl())
        return GetEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateCreateEndpointUseCase(): CreateEndpointUseCase {
        val endpointRepository = getRepositoryImpl(getPropertyProviderImpl())
        return CreateEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateUpdateEndpointUseCase(): UpdateEndpointUseCase {
        val endpointRepository = getRepositoryImpl(getPropertyProviderImpl())
        return UpdateEndpointUseCase(endpointRepository)
    }

    @Produces
    fun instantiateDeleteEndpointUseCase(): DeleteEndpointUseCase {
        val endpointRepository = getRepositoryImpl(getPropertyProviderImpl())
        return DeleteEndpointUseCase(endpointRepository)
    }

    private fun getRepositoryImpl(propertyProvider: PropertyProvider): EndpointRepository {
        if (!this::repository.isInitialized) {
            repository = SqliteRepository(propertyProvider.get("database.sqlite.file-path"))
            (repository as SqliteRepository).connect()
        }
        return repository
    }

    private fun getPropertyProviderImpl(): PropertyProvider {
        return ApplicationPropertyProvider("application-dev.properties")
    }

    @PreDestroy
    fun shutDown() {
        if (this::repository.isInitialized){
            (repository as SqliteRepository).disconnect()
        }
    }
}