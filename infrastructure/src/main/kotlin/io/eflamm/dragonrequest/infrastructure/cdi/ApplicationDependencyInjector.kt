package io.eflamm.dragonrequest.infrastructure.cdi

import io.eflamm.dragonrequest.application.usecase.*
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.domain.repository.EndpointRepository
import io.eflamm.dragonrequest.infrastructure.api.EndpointsController
import io.eflamm.dragonrequest.infrastructure.cdi.properties.ApplicationPropertiesFileProvider
import io.eflamm.dragonrequest.infrastructure.cdi.properties.PropertyProvider
import io.eflamm.dragonrequest.logger.slf4j.SLF4JLogger
import io.eflamm.dragonrequest.repository.sqlite.SqliteRepository

fun main(args: Array<String>) {
    ApplicationDependencyInjector().startApplication(args.toList())
}

class ApplicationDependencyInjector {

    private lateinit var repository: EndpointRepository
    private lateinit var propertyProvider: PropertyProvider
    private lateinit var logger: Logger

    fun startApplication(startArguments: List<String>) {
        logger = instantiateLogger(this::class.java.simpleName)
        val profile = getProfile(startArguments)
        propertyProvider = instantiatePropertyProviderImpl(getPropertyFileName(profile))
        instantiateController(propertyProvider).start()
    }

    private fun getProfile(startupArguments: List<String>): String {
        // TODO enum for authorized profiles
        var profile = "default"
        if (startupArguments.isNotEmpty()) {
            if(listOf("dev", "testing").contains(startupArguments[0])) {
                profile = startupArguments[0]
            }
        }
        logger.info("Profile used $profile")
        return profile
    }

    private fun getPropertyFileName(profile: String): String {
        return when(profile) {
            "dev" -> "application-dev.properties"
            "testing" -> "application-testing.properties"
            else -> "application.properties"
        }
    }

    private fun instantiateController(propertyProvider: PropertyProvider): EndpointsController {
        return EndpointsController(
            instantiateGetEndpointsUseCase(propertyProvider),
            instantiateGetSingleEndpointUseCase(propertyProvider),
            instantiateCreateEndpointUseCase(propertyProvider),
            instantiateUpdateEndpointUseCase(propertyProvider),
            instantiateDeleteEndpointUseCase(propertyProvider),
            instantiateLogger(EndpointsController::class.java.simpleName)
        )
    }

    private fun instantiateGetEndpointsUseCase(propertyProvider: PropertyProvider): GetEndpointsUseCase {
        val endpointRepository = instantiateRepositoryImpl(propertyProvider)
        return GetEndpointsUseCase(endpointRepository)
    }

    private fun instantiateGetSingleEndpointUseCase(propertyProvider: PropertyProvider): GetSingleEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(propertyProvider)
        return GetSingleEndpointUseCase(endpointRepository)
    }

    private fun instantiateCreateEndpointUseCase(propertyProvider: PropertyProvider): CreateEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(propertyProvider)
        return CreateEndpointUseCase(endpointRepository)
    }

    private fun instantiateUpdateEndpointUseCase(propertyProvider: PropertyProvider): UpdateEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(propertyProvider)
        return UpdateEndpointUseCase(endpointRepository)
    }

    private fun instantiateDeleteEndpointUseCase(propertyProvider: PropertyProvider): DeleteEndpointUseCase {
        val endpointRepository = instantiateRepositoryImpl(propertyProvider)
        return DeleteEndpointUseCase(endpointRepository)
    }

    private fun instantiateRepositoryImpl(propertyProvider: PropertyProvider): EndpointRepository {
        if (!this::repository.isInitialized) {
            repository = SqliteRepository(propertyProvider.get("database.sqlite.file-path"), instantiateLogger(SqliteRepository::class.java.simpleName))
            (repository as SqliteRepository).connect()
        }
        return repository
    }

    private fun instantiatePropertyProviderImpl(propertiesFileName: String): PropertyProvider {
        return ApplicationPropertiesFileProvider(propertiesFileName)
    }

    private fun instantiateLogger(className: String): Logger {
        return SLF4JLogger(className)
    }
}