package io.eflamm.dragonrequest.domain.model.endpoint

class Path(val pathSegments: List<String>) {
    // TODO handle empty path
    companion object {
        fun create(): Path {
            return Path(emptyList())
        }
        fun fromString(aggregatedPath: String): Path {
            return Path(aggregatedPath.split("/").filter { s -> s.isNotEmpty() }.toList())
        }
    }
    fun get(): List<String> {
        return pathSegments
    }
    fun aggregate(): String {
        return if(pathSegments.isNotEmpty()) {
           "/" + pathSegments.joinToString ("/" )
        } else {
            ""
        }
    }
}