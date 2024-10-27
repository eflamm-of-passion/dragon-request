package io.eflamm.domain.model.endpoint;

enum class Protocol(val value: String) {
    HTTP("http"), HTTPS("https");

    companion object {
        fun fromString(value: String): Protocol {
            // TODO check that
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: HTTP
        }
    }

    fun get(): String {
        return value
    }

}
