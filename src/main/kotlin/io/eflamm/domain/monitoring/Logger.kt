package io.eflamm.domain.monitoring

interface Logger {
    fun info(message: String)
    fun debug(message: String)
    fun warn(message: String)
    fun error(message: String, e: Throwable? = null)
}