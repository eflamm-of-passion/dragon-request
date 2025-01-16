package io.eflamm.dragonrequest.infrastructure.cdi.properties

interface PropertyProvider {
    fun get(key: String): String
}