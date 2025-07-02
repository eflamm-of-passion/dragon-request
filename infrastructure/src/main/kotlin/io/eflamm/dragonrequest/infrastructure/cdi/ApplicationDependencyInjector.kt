package io.eflamm.dragonrequest.infrastructure.cdi

import io.eflamm.dragonrequest.application.usecase.ApiFileUseCases
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.domain.repository.ApiFilesRepository
import io.eflamm.dragonrequest.infrastructure.api.EndpointsController
import io.eflamm.dragonrequest.infrastructure.cdi.properties.ApplicationPropertiesFileProvider
import io.eflamm.dragonrequest.infrastructure.cdi.properties.PropertyProvider
import io.eflamm.dragonrequest.logger.slf4j.SLF4JLogger
import io.eflamm.dragonrequest.repository.mongodb.MongoConnectorImpl
import io.eflamm.dragonrequest.repository.mongodb.MongoRepository
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    ApplicationDependencyInjector().startApplication(args.toList())
}

class ApplicationDependencyInjector {
    private lateinit var repository: MongoRepository
    private lateinit var logger: Logger

    fun startApplication(startArguments: List<String>) {
        printOutAppName()

        logger = instantiateLogger(this::class.java.simpleName)

        val profile = getProfile(startArguments)
        try {
            val propertyProvider = instantiatePropertyProviderImpl(getPropertyFileName(profile), getPropertyFileName("default"))
            instantiateController(propertyProvider).startHttpServerOnPort(propertyProvider.httpServerPort())
        } catch (e: RuntimeException) {
            logger.error("Failed to load the properties, exiting application")
            exitProcess(1)
        }
    }

    private fun getProfile(startupArguments: List<String>): String {
        val authorizedProfiles = listOf("dev", "integration-testing")
        var profile = "default"
        if (startupArguments.isNotEmpty()) {
            val profileAppArgument = startupArguments[0]
            if (authorizedProfiles.contains(profileAppArgument)) {
                profile = profileAppArgument
            } else {
                logger.warn("Unauthorized profile : $profileAppArgument")
            }
        }
        logger.info("Profile used : $profile")
        return profile
    }

    private fun getPropertyFileName(profile: String): String =
        when (profile) {
            "dev" -> "application-dev.properties"
            "integration-testing" -> "application-integration-testing.properties"
            "default" -> "application.properties"
            else -> "application.properties"
        }

    private fun instantiateController(propertyProvider: PropertyProvider): EndpointsController {
        logInstantiation(EndpointsController::class.java.simpleName, "controller")
        return EndpointsController(
            instantiateApiFileUseCases(propertyProvider),
            instantiateLogger(EndpointsController::class.java.simpleName),
        )
    }

    private fun instantiateApiFileUseCases(propertyProvider: PropertyProvider): ApiFileUseCases {
        val repository = instantiateApiFileRepositoryImpl(propertyProvider)
        return ApiFileUseCases(repository)
    }

    private fun instantiateApiFileRepositoryImpl(propertyProvider: PropertyProvider): ApiFilesRepository = instantiateMongoRepository(propertyProvider)

    private fun instantiateMongoRepository(propertyProvider: PropertyProvider): MongoRepository {
        if (!this::repository.isInitialized) {
            repository =
                MongoRepository(
                    MongoConnectorImpl(
                        propertyProvider.mongodbAddress(),
                        propertyProvider.mongodbDatabaseName(),
                        propertyProvider.mongodbCollectionName(),
                        instantiateLogger(MongoConnectorImpl::class.java.simpleName),
                    ),
                    instantiateLogger(MongoRepository::class.java.simpleName),
                )
            (repository).connect()
            logInstantiation(MongoRepository::class.java.simpleName, "EndpointRepository")
        }
        return repository
    }

    private fun instantiatePropertyProviderImpl(
        propertiesFileName: String,
        fallbackPropertiesFileName: String,
    ): PropertyProvider {
        logInstantiation(ApplicationPropertiesFileProvider::class.java.simpleName, "PropertyProvider")
        val provider =
            ApplicationPropertiesFileProvider(
                propertiesFileName,
                fallbackPropertiesFileName,
                instantiateLogger(ApplicationPropertiesFileProvider::class.java.simpleName),
            )
        return provider
    }

    private fun instantiateLogger(forClassName: String): Logger {
        val logger = SLF4JLogger(forClassName)
        logger.info("instantiate ${SLF4JLogger::class.java.simpleName} as Logger in $forClassName")
        return logger
    }

    private fun logInstantiation(
        className: String,
        asInterfaceName: String,
    ) {
        logger.info("instantiate $className as $asInterfaceName")
    }

    private fun printOutAppName() {
        println(
            """   
                             ██████████                                                           
                            ░░███░░░░███                                                     
                             ░███   ░░███ ████████   ██████    ███████  ██████  ████████     
                             ░███    ░███░░███░░███ ░░░░░███  ███░░███ ███░░███░░███░░███    
                             ░███    ░███ ░███ ░░░   ███████ ░███ ░███░███ ░███ ░███ ░███    
                             ░███    ███  ░███      ███░░███ ░███ ░███░███ ░███ ░███ ░███    
                             ██████████   █████    ░░████████░░███████░░██████  ████ █████   
                            ░░░░░░░░░░   ░░░░░      ░░░░░░░░  ░░░░░███ ░░░░░░  ░░░░ ░░░░░    
                                                              ░██ ░███                       
                                              ██████          ░░█████                █████   
                                            ███░░░░███         ░░░░░░               ░░███    
                        ████████   ██████  ███    ░░███ █████ ████  ██████   █████  ███████  
                       ░░███░░███ ███░░███░███     ░███░░███ ░███  ███░░███ ███░░  ░░░███░   
                        ░███ ░░░ ░███████ ░███   ██░███ ░███ ░███ ░███████ ░░█████   ░███    
                        ░███     ░███░░░  ░░███ ░░████  ░███ ░███ ░███░░░   ░░░░███  ░███ ███
                        █████    ░░██████  ░░░██████░██ ░░████████░░██████  ██████   ░░█████ 
                       ░░░░░      ░░░░░░     ░░░░░░ ░░   ░░░░░░░░  ░░░░░░  ░░░░░░     ░░░░░ 
        """,
        )
    }
}
