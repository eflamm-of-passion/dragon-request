package io.eflamm.dragonrequest.domain.model.endpoint

@JvmInline
value class QueryParameters(
    val queryParameters: Map<String, String>,
) {
    companion object {
        fun create(): io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters =
            io.eflamm.dragonrequest.domain.model.endpoint
                .QueryParameters(emptyMap())

        fun fromString(aggregatedFromString: String): io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters =
            io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters(
                aggregatedFromString
                    .removePrefix("?")
                    .split("&")
                    .filter { it.isNotEmpty() && it.indexOf('=') in 1 until it.length - 1 }
                    .associate {
                        val (key, value) = it.split("=")
                        key to value
                    },
            )
    }

    fun aggregate(): String {
        var aggregatedQueryParams = ""
        if (queryParameters.isNotEmpty()) {
            aggregatedQueryParams = "?" +
                queryParameters
                    .map { queryParam -> queryParam.key + "=" + queryParam.value }
                    .joinToString("&")
        }
        return aggregatedQueryParams
    }

    override fun toString(): String = aggregate()
}
