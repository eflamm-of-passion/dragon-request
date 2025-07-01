package io.eflamm.dragonrequest.infrastructure.cdi.properties

import io.eflamm.dragonrequest.domain.monitoring.Logger
import java.util.Properties
import kotlin.reflect.KClass

class ApplicationPropertiesFileProvider(
    propertiesFilePath: String,
    fallbackPropertiesFilePath: String,
    private val logger: Logger,
) : PropertyProvider {
    private val properties: Properties = Properties()

    enum class Property(
        val keyInPropertiesFile: String,
        val expectedType: KClass<*>,
    ) {
        MONGODB_DATABASE_NAME("database.mongodb.database", String::class),
        MONGODB_COLLECTION_NAME("database.mongodb.collection", String::class),
        MONGODB_ADDRESS("database.mongodb.address", String::class),
        SQLITE_FILE_PATH("database.sqlite.file-path", String::class),
        HTTP_SERVER_PORT("http-server.port", Int::class),
    }

    init {
        areThereMissingFiles(listOf(fallbackPropertiesFilePath, propertiesFilePath))
        loadPropertiesFromFiles(listOf(fallbackPropertiesFilePath, propertiesFilePath))
        validateProperties()
    }

    private fun get(key: String): String? = properties.getProperty(key)

    private fun loadPropertiesFromFiles(filePaths: List<String>) {
        filePaths.forEach { filePath ->
            this::class.java.classLoader
                .getResourceAsStream(filePath)
                .use { this.properties.load(it) }
        }
    }

    private fun areThereMissingFiles(filePaths: List<String>) {
        var numberOfMissingFiles = 0
        filePaths.forEach { filePath ->
            if (this::class.java.classLoader.getResource(filePath) == null) {
                numberOfMissingFiles += 1
                logger.warn("The property file $filePath is missing")
            }
        }
        if (filePaths.size == numberOfMissingFiles) {
            logger.error("All the files are missing")
            throw RuntimeException()
        }
    }

    private fun validateProperties() {
        val errorMessages: MutableList<String> = mutableListOf()
        Property.entries.forEach { property ->
            get(property.keyInPropertiesFile)?.let { propertyValue ->
                validatePropertyType(property.keyInPropertiesFile, propertyValue, property.expectedType)?.let { errorMessage ->
                    errorMessages.add(errorMessage)
                }
            } ?: run {
                errorMessages.add("Missing property ${property.keyInPropertiesFile}")
            }
        }
        if (errorMessages.isNotEmpty()) {
            logger.error("Error while loading properties", errorMessages)
            throw RuntimeException()
        }
    }

    private fun validatePropertyType(
        propertyKey: String,
        propertyValue: String,
        expectedType: KClass<*>,
    ): String? {
        var errorMessage: String? = null
        when (expectedType) {
            String::class -> {
                if (propertyValue.isBlank()) {
                    errorMessage = "The property $propertyKey is empty"
                }
            }
            Int::class -> {
                if (propertyValue.toIntOrNull() == null) {
                    errorMessage = "The property $propertyKey has the value $propertyValue, which is not an integer"
                }
            }
            else -> {
                // cannot happen
            }
        }
        return errorMessage
    }

    override fun mongodbDatabaseName(): String = this.get(Property.MONGODB_DATABASE_NAME.keyInPropertiesFile)!!

    override fun mongodbCollectionName(): String = this.get(Property.MONGODB_COLLECTION_NAME.keyInPropertiesFile)!!

    override fun mongodbAddress(): String = this.get(Property.MONGODB_ADDRESS.keyInPropertiesFile)!!

    override fun sqliteFilePath(): String = this.get("database.sqlite.file-path")!!

    override fun httpServerPort(): Int = this.get(Property.HTTP_SERVER_PORT.keyInPropertiesFile)!!.toInt()
}
