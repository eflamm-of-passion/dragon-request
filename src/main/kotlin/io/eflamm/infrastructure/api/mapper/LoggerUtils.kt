package io.eflamm.infrastructure.api.mapper

import com.google.gson.GsonBuilder

abstract class LoggerUtils {
    companion object Methods {
        fun displayAsJson(input: Any): String{
            return GsonBuilder().disableHtmlEscaping().create().toJson(input)
        }
    }
}
