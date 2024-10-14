package io.eflamm.domain.model.endpoint;

enum class Protocol(val value: String) {
    HTTP("http"), HTTPS("https");

    fun get(): String {
        return value
    }

    fun set(valueAsString : String): Protocol {
        // TODO check that
        return Protocol.valueOf(valueAsString)
    }
}
