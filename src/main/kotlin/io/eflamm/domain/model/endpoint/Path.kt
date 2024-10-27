package io.eflamm.domain.model.endpoint

class Path(val pathSegments: List<String>) {
    // TODO handle empty path
    companion object {
        fun create(): Path {
            return Path(emptyList())
        }
        fun fromString(aggregatedPath: String): Path {
            return Path(aggregatedPath.split("/"))
        }
    }
    fun get(): List<String> {
        return pathSegments
    }
    fun aggregate(): String {
        return pathSegments.joinToString { "/" }
    }
}