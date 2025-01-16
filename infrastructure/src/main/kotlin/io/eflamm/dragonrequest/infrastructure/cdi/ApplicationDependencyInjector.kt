package io.eflamm.dragonrequest.infrastructure.cdi

import io.eflamm.dragonrequest.application.usecase.*
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.domain.repository.EndpointRepository
import io.eflamm.dragonrequest.infrastructure.api.EndpointsController
import io.eflamm.dragonrequest.infrastructure.cdi.properties.ApplicationPropertyProvider
import io.eflamm.dragonrequest.infrastructure.cdi.properties.PropertyProvider
import io.eflamm.dragonrequest.logger.slf4j.SLF4JLogger
import io.eflamm.dragonrequest.repository.sqlite.SqliteRepository

fun main() {
    ApplicationDependencyInjector().startApplication()
}

class ApplicationDependencyInjector {

    private lateinit var repository: EndpointRepository

    fun startApplication() {
        instantiateController().start()
    }

    private fun instantiateController(): EndpointsController {
        return EndpointsController(
            instantiateGetEndpointsUseCase(),
            instantiateGetSingleEndpointUseCase(),
            instantiateCreateEndpointUseCase(),
            instantiateUpdateEndpointUseCase(),
            instantiateDeleteEndpointUseCase(),
            instantiateLogger()
        )
    }

    private fun instantiateGetEndpointsUseCase(): GetEndpointsUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return GetEndpointsUseCase(endpointRepository)
    }

    private fun instantiateGetSingleEndpointUseCase(): GetSingleEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return GetSingleEndpointUseCase(endpointRepository)
    }

    private fun instantiateCreateEndpointUseCase(): CreateEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return CreateEndpointUseCase(endpointRepository)
    }

    private fun instantiateUpdateEndpointUseCase(): UpdateEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return UpdateEndpointUseCase(endpointRepository)
    }

    private fun instantiateDeleteEndpointUseCase(): DeleteEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(instantiatePropertyProviderImpl())
        return DeleteEndpointUseCase(endpointRepository)
    }

    private fun instantiateRepositoryImpl(propertyProvider: PropertyProvider): EndpointRepository {
        if (!this::repository.isInitialized) {
            repository = SqliteRepository(propertyProvider.get("database.sqlite.file-path"))
            (repository as SqliteRepository).connect()
        }
        return repository
    }

    private fun instantiatePropertyProviderImpl(): PropertyProvider {
        return ApplicationPropertyProvider("application-dev.properties")
    }

    private fun instantiateLogger(): Logger {
        return SLF4JLogger()
    }

}