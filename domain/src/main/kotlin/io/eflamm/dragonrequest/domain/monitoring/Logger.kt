package io.eflamm.dragonrequest.domain.monitoring

interface Logger {
    fun info(message: String)
    fun debug(message: String)
    fun warn(message: String)
    fun error(message: String, detailMessages: List<String>? = null, e: Throwable? = null)
}