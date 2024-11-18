package io.eflamm.dragonrequest.domain.model.endpoint

class Port(val port: Int = 80) {
    fun get(): Int {
        return port
    }
    fun isDefaultPort(): Boolean {
        return port == 80 || port == 443
    }
}