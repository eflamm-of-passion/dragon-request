package io.eflamm.dragonrequest.domain.slf4jlogger

import io.eflamm.dragonrequest.domain.Logger

class Slf4jLogger(
    private val delegate: org.slf4j.Logger
): Logger {
    override fun debug(message: () -> String) {
        if(delegate.isDebugEnabled) delegate.debug(message())
    }

    override fun info(message: () -> String) {
        if(delegate.isInfoEnabled) delegate.info(message())
    }

    override fun warn(message: () -> String) {
        if(delegate.isWarnEnabled) delegate.warn(message())
    }

    override fun error(message: () -> String, cause: Throwable?) {
        delegate.debug(message(), cause)
    }
}