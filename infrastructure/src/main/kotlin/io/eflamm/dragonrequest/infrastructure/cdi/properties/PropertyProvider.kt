package io.eflamm.dragonrequest.infrastructure.cdi.properties

interface PropertyProvider {
    fun sqliteFilePath(): String
    fun httpServerPort(): Int
}