package io.eflamm.dragonrequest.infrastructure.cdi.properties

import java.util.Properties

class ApplicationPropertiesFileProvider(propertiesFilePath: String): PropertyProvider {
    // TODO be able to load properties from many properties files

    private val properties: Properties = Properties()

    init {
        val inputStream = this::class.java.classLoader.getResourceAsStream(propertiesFilePath)
        inputStream.use { properties.load(it) }
    }

    override fun get(key: String): String {
        return properties.getProperty(key)
    }

}