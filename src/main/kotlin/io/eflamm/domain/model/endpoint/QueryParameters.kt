package io.eflamm.domain.model.endpoint

class QueryParameters(val queryParameters: Map<String, String>) {
    fun aggregate(): String {
        var aggregatedQueryParams = ""
        if(queryParameters.isNotEmpty()) {
            aggregatedQueryParams = queryParameters.map { queryParam -> queryParam.key + "=" + queryParam.value }
                .joinToString { "&" }.dropLast(1)
        }
        return aggregatedQueryParams
    }
}