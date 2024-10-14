package io.eflamm.domain.model.endpoint

class Path(val pathSegments: List<String>) {
    fun get(): List<String> {
        return pathSegments
    }
    fun aggregate(): String {
        return pathSegments.joinToString { "/" }
    }
}