package io.eflamm.dragonrequest.domain.model.endpoint

@JvmInline
value class Path(
    val pathSegments: List<String>,
) {
    // TODO handle empty path
    companion object {
        fun create(): Path = Path(emptyList())

        fun fromString(aggregatedPath: String): Path = Path(aggregatedPath.split("/").filter { s -> s.isNotEmpty() }.toList())
    }

    fun get(): List<String> = pathSegments

    fun aggregate(): String =
        if (pathSegments.isNotEmpty()) {
            "/" + pathSegments.joinToString("/")
        } else {
            ""
        }

    override fun toString(): String = aggregate()
}
