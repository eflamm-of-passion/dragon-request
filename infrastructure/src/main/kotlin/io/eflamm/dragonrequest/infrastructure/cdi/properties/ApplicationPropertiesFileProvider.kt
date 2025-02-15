package io.eflamm.dragonrequest.infrastructure.cdi.properties

import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop
import java.util.Properties

class ApplicationPropertiesFileProvider(propertiesFilePath: String, fallbackPropertiesFilePath: String): PropertyProvider {

    private val properties: Properties = Properties()

    init {
        var inputStream = this::class.java.classLoader.getResourceAsStream(fallbackPropertiesFilePath)
        inputStream.use { properties.load(it) }
        inputStream?.close()
        inputStream = this::class.java.classLoader.getResourceAsStream(propertiesFilePath)
        inputStream.use { properties.load(it) }
        inputStream?.close()
    }

    override fun get(key: String): String {
        return properties.getProperty(key)
    }
}