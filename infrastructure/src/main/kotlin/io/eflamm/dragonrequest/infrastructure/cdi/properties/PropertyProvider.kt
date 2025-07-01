package io.eflamm.dragonrequest.infrastructure.cdi.properties

interface PropertyProvider {
    fun mongodbDatabaseName(): String

    fun mongodbCollectionName(): String

    fun mongodbAddress(): String

    fun sqliteFilePath(): String

    fun httpServerPort(): Int
}
