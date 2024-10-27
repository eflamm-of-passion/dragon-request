package io.eflamm.infrastructure.cdi.properties

interface PropertyProvider {
    fun get(key: String): String
}