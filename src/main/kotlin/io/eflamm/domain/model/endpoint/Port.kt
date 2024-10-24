package io.eflamm.domain.model.endpoint

class Port(val port: Int = 80) {
    fun get(): Int {
        return port
    }
}