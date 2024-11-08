package io.eflamm.application.mapper

import com.google.gson.GsonBuilder

abstract class LoggerUtils {
    companion object Methods {
        fun displayAsJson(input: Any): String{
            return GsonBuilder().disableHtmlEscaping().create().toJson(input)
        }
    }
}
