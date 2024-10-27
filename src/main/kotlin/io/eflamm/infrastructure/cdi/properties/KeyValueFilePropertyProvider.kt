package io.eflamm.infrastructure.cdi.properties

import java.io.FileInputStream
import java.util.Properties

class KeyValueFilePropertyProvider: PropertyProvider {
    // TODO be able to load properties from many properties files

    private val properties: Properties = Properties()

    object DefaultValues {
        const val DEFAULT_PROPERTIES_FILES = "application-dev.properties"
    }

    init {
        val inputStream = this::class.java.classLoader.getResourceAsStream(DefaultValues.DEFAULT_PROPERTIES_FILES)
        inputStream.use { properties.load(it) }
    }

    override fun get(key: String): String {
        return properties.getProperty(key)
    }

}